package de.bv.utils.dbiep.tfanalyzer.processor;

import com.google.common.collect.Lists;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOption;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOptionBuilder;
import de.bv.utils.dbiep.tfanalyzer.model.ColumnConfiguration;
import de.bv.utils.dbiep.tfanalyzer.model.Configuration;
import de.bv.utils.dbiep.tfanalyzer.model.FileConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.XSlf4j;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Directory processor is a class which scans a given folder for files to be imported.
 *
 * @version 2020-08
 * @since   2020-08
 * @author Bernhard Volz - initial implementation
 */
@XSlf4j
@Getter
public class DirectoryProcessor {

    private Path inputPath;
    private Configuration configuration;

    public List<CommandLineOption<?>> getCommandLineOptions() {
        return Lists.newArrayList(
     //           CommandLineOptionBuilder.createIntegerOption(null, "directory.scanDepth", "todo", false, "n", 1)
        );
    }

    public DirectoryProcessor init(Configuration configuration) {
        this.configuration = configuration;
        this.inputPath = Paths.get(getConfiguration().getDirectory());

        return this;
    }

    public DirectoryProcessor process() throws IOException {
        log.entry();
        log.info("start scanning folder {} for files with extension {} ...",
                getConfiguration().getDirectory(),
                getConfiguration().getFileExtension());

        // check, if directory exists
        if (!Files.isDirectory(getInputPath()) || Files.notExists(getInputPath())) {
            log.error("folder {} is not a directory or does not exist! exiting ...", getConfiguration().getDirectory());
            System.exit(-1);
        }

        StreamCounter streamCounter = new StreamCounter();
        /*ForkJoinPool customPool = new ForkJoinPool(8);

        customPool.submit(
                () -> {
                    try {
                        Files.find(getInputPath(), 1, (p, a) -> {
                            String extension = com.google.common.io.Files.getFileExtension(p.getFileName().toString());
                            return a.isRegularFile() && getConfiguration().getFileExtension().equals(extension);
                        })
                                //.parallel()
                                .peek(f -> log.info("found file {} ({} files so far): start analyzing ...", f, streamCounter.incrementAndGet()))
                                .forEach(this::processFile);
                    } catch (IOException e) {
                    }
                }
        ).join();*/

        Files.find(getInputPath(), 1, (p, a) -> {
                    String extension = com.google.common.io.Files.getFileExtension(p.getFileName().toString());
                    return a.isRegularFile() && getConfiguration().getFileExtension().equals(extension);
                })
                .parallel()
                .peek(f -> log.info("found file {} ({} files so far): start analyzing ...", f, streamCounter.incrementAndGet()))
                .forEach(this::processFile);

        log.info("finished scanning folder {}.", getConfiguration().getDirectory());
        log.exit();
        return this;
    }

    @Accessors(chain = true)
    private static class StreamCounter {

        @Getter(AccessLevel.PACKAGE)
        private int counterValue = 0;

        StreamCounter reset() {
            counterValue = 0;
            return this;
        }

        int getAndIncrement() {
            return counterValue++;
        }

        int incrementAndGet() {
            return ++counterValue;
        }
    }

    protected void processFile(final Path csvFile) {
        FileConfiguration csvFileCfg = new FileConfiguration()
                .setFilename(com.google.common.io.Files.getNameWithoutExtension(csvFile.toString()))
                .setLinesToSkip(0)
                .setPath(csvFile.toAbsolutePath().toString())
                .setHeaderPresent(getConfiguration().isHeader())
                ;

        getConfiguration().getFileConfigurations().add(csvFileCfg);

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFile.toUri()), getConfiguration().getCharset());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withSkipLines(csvFileCfg.getLinesToSkip())
                     .withCSVParser(new CSVParserBuilder()
                             .withSeparator(getConfiguration().getSeparationCharacter())
                             .withEscapeChar(getConfiguration().getEscapeCharacter())
                             .withQuoteChar(getConfiguration().getQuotationCharacter())
                             .build()
                     ).build()
        ) {
            // need to find the number of columns and, if possible, the column names
            final String[] firstLine = csvReader.peek();
            if (firstLine.length == 0) {
                csvFileCfg.setEmpty(true);
            } else {
                int n = 0;

                for(String column : firstLine) {
                    ColumnConfiguration columnCfg = new ColumnConfiguration()
                            .setId(n)
                            .setName(csvFileCfg.isHeaderPresent() && !column.trim().isEmpty() ? column.trim() : "C_" + n)
                            .setCsvFile(csvFileCfg);
                    csvFileCfg.getColumns().add(columnCfg);

                    log.debug("\t column: {}, name = {}", n, columnCfg.getName());

                    n++;
                }
            }

            // skip header, if present
            if (csvFileCfg.isHeaderPresent()) {
                csvReader.readNext();
            }

            // create column heuristic
            ColumnHeuristic columnHeuristic = new ColumnHeuristic(
                    csvFileCfg,
                    getConfiguration());

            // now iterate over the single lines try to find a proper type (if required)
            if (getConfiguration().getGuess() != 0) {
                Iterator<String[]> iterator = csvReader.iterator();
                Iterable<String[]> iterable = () -> iterator;

                StreamSupport.stream(iterable.spliterator(), false)
                        .limit(getConfiguration().getGuess() != -1 ? getConfiguration().getGuess() : Long.MAX_VALUE)
                        .parallel()
                        .forEach(columnHeuristic::process);

                int n = 0;
                columnHeuristic.finishProcessing();
                for (ColumnHeuristic.ColumnInfo info : columnHeuristic.getColumns()) {
                    ColumnConfiguration csvColumnCfg = csvFileCfg.getColumns().get(n++);
                    csvColumnCfg
                            .setMappedType(info.getMappedType())
                            .setNullable(info.isEmpty())
                            .setEmpty(info.isEmptyColumn())
                            .setMaxLength(info.getMaxEntryLength());
                }
            } else {
                csvFileCfg.getColumns()
                        .forEach(c -> {
                            c.setMappedType(Types.NVARCHAR);
                            c.setMaxLength(255);
                        });
            }

            // drop empty columns at the end (if required)
            if (getConfiguration().isDropEmptyColumns()) {
                ColumnConfiguration csvColumnCfg = csvFileCfg.getColumns().get(csvFileCfg.getColumns().size() - 1);
                if (csvColumnCfg.isEmpty()) {
                    log.info("dropping empty column {} with name {} in file {} ...",
                            csvColumnCfg.getId(),
                            csvColumnCfg.getName(),
                            csvFile.toString());
                }
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        log.debug("result: {}", csvFileCfg);

        // print a short summary to the info channel
        log.info("analysis result for " + csvFileCfg.getSummary());
    }

}

