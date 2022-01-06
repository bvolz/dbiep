package de.bv.utils.dbiep.tfanalyzer;

import de.bv.utils.dbiep.tfanalyzer.cmdline.CommandLineOptionsParser;
import de.bv.utils.dbiep.tfanalyzer.model.Configuration;
import de.bv.utils.dbiep.tfanalyzer.model.ModelSerializer;
import de.bv.utils.dbiep.tfanalyzer.processor.DirectoryProcessor;

import java.io.IOException;

public class TFAnalyzer {
    public static void main(String[] args) {
        CommandLineOptionsParser cmdLineParser = new CommandLineOptionsParser(args);
        Configuration configuration = cmdLineParser.getConfiguration();

        try {
            parseInput(configuration);

            ModelSerializer modelSerializer = new ModelSerializer();
            if (configuration.isWriteCfg()) {
                modelSerializer.serialize(configuration.getCfgFile(), configuration);
            } else {
                modelSerializer.serialize(configuration);
            }

            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();

            System.exit(-1);
        }

        cmdLineParser.printUsage();
    }

    private static void parseInput(final Configuration configuration) throws IOException {
        DirectoryProcessor directoryProcessor = new DirectoryProcessor()
                .init(configuration)
                .process()
        ;
    }
}
