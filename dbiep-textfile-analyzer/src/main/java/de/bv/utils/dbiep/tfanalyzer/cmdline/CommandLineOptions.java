package de.bv.utils.dbiep.tfanalyzer.cmdline;

import de.bv.utils.dbiep.tfanalyzer.model.Configuration;
import de.bv.utils.dbiep.tfanalyzer.util.TFAnalyzerUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration representing all possible command line options of TFAnalyzer
 *
 * @since 2020-12
 * @version 2022-01
 * @author Bernhard Volz
 */
@Getter
@AllArgsConstructor
public enum CommandLineOptions {

    // general parameters
    DRY_RUN("d", "dry", "if set, does not alter anything but merely prints changes on the console", (v, o, c) -> c.setDryRun(true)),
    WRITE_CONFIG("wc", "writeCfg", null, false, "writes configuration to the specified file", true, "file", String.class, (v, o, c) -> {
        c.setCfgFile(o.convertValue(v));
        c.setWriteCfg(true);
    }),
    INPUT_DIRECTORY("i", "inputFolder", null, false, "specifies the folder from which data is loaded", true, "folder", String.class, (v, o, c) -> c.setDirectory(o.convertValue(v))),
    FILE_EXTENSION("e", "extension", "csv", false, "file extension of input files within the input directory, defaults to 'csv'", true, "ext", String.class, (v, o, c) -> c.setFileExtension(o.convertValue(v))),

    // settings for heuristic
    GUESS_MAX("g", "guessMax", 1000, false, "maximum number of lines to read from each input file for guessing data types, defaults to '1000' (special values: -1 means all lines, 0 means do not guess at all", true, "n", Integer.class, (v, o, c) -> c.setGuess(o.convertValue(v))),
    COLUMN_HEADINGS("headings", "columnHeadings", "if specified, input files contain the names of the columns within the first line", (v, o, c) -> c.setHeader(true)),
    SEPARATOR("sep", "separatorChar", ',', false, "separation character, defaults to ','", true, "c", Character.class, (v, o, c) -> c.setSeparationCharacter(TFAnalyzerUtils.convertString2Character(v))),
    ESCAPE_CHARACTER("escapeChar", "escapeChar", '\\', false, "escape character, defaults to '\\'", true, "c", Character.class, (v, o, c) -> c.setEscapeCharacter(TFAnalyzerUtils.convertString2Character(v))),
    QUOTATION_CHARACTER("quotationChar", "quotationChar", '"', false, "quotation character, defaults to '\"'", true, "c", Character.class, (v, o, c) -> c.setQuotationCharacter(TFAnalyzerUtils.convertString2Character(v))),
    CHARSET("charset", "charset", "UTF-8", false, "charset for files, defaults to 'UTF-8'", true, "name", String.class, (v, o, c) -> c.setCharset(o.convertValue(v))),

    DATE_FORMAT("dateFormat", "dateFormat", "yyyy-MM-dd HH:mm:ss", false, "date format used for reading timestamp data", true, "format", String.class, (v, o, c) -> c.setDateFormat(o.convertValue(v))),

    // actions
    RECREATE_SCHEMA("recreateSchema", "recreateSchema", "if present, the schema is recreated, i.e. tables are dropped and then created again", (v, o, c) -> c.setRecreateSchema(true)),
    TRUNCATE_SCHEMA("truncateSchema", "truncateSchema", "if present, the schema is truncated, i.e. tables are truncated", (v, o, c) -> c.setTruncateTables(true)),

    // permissive mode flags
    PERMISSIVE_MODE("permissive", "permissiveMode", null, false, "if present, certain errors can be ignored; allowed errors (separated by comma): additionalColumns", true, "modes", String.class, (v, o, c) -> {
        if (v.contains("additionalColumns")) {
            c.setPermissiveAdditionalColumns(true);
        }
    })
    ;

    CommandLineOptions(String shortName, String longName, String description, TriConsumer<String, CommandLineOptions, Configuration> handler) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;

        this.required = false;
        this.hasArg = false;
        this.wrapperClass = Boolean.class;
        this.configurationSetter = handler;
    }

    private final String shortName;
    private final String longName;
    private Object defaultValue;
    private final boolean required;

    private final String description;
    private final boolean hasArg;
    private String argName;

    private final Class<?> wrapperClass;

    private final TriConsumer<String, CommandLineOptions, Configuration> configurationSetter;

    String getStringRepresentation() {
        return "{" +
                "type='" + wrapperClass.getSimpleName() + '\'' +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", defaultValue=" + defaultValue +
                ", required=" + required +
                ", description='" + description + '\'' +
                ", hasArg=" + hasArg +
                ", argName='" + argName + '\'' +
                '}';
    }

    @SuppressWarnings("unchecked")
    private <T> T convertValue(String value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (T) getWrapperClass()
                .getDeclaredConstructor(String.class)
                .newInstance(value);
    }

    public static Optional<CommandLineOptions> fromShortName(final String shortName) {
        return Arrays.stream(CommandLineOptions.class.getEnumConstants())
                .filter(e -> e.getShortName().equals(shortName))
                .findFirst();
    }

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void apply(T t, U u, V v) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    }
}