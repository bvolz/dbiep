package de.bv.utils.dbiep.cmdline;

import com.google.common.collect.Maps;
import de.bv.utils.dbiep.DBIEP;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOption;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOptionsProvider;
import de.bv.utils.dbiep.spi.configuration.Configuration;
import de.bv.utils.dbiep.tfanalyzer.DBIEPConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.cli.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@XSlf4j
public class CommandLineOptionsParser {
    @Getter(AccessLevel.PROTECTED)
    private final String[] args;

    @Getter(AccessLevel.PUBLIC)
    private final Options cmdLineOptions = new Options();

    @Getter(AccessLevel.PROTECTED)
    private Map<Option, CommandLineOption<?>> commandLineOptionsMap = Maps.newHashMap();

    /**
     * Constructs a new object from given command line arguments.
     * @param args array of {@code String} values representing the command line parameters
     */
    public CommandLineOptionsParser(String[] args) {
        log.entry((Object[]) args);

        this.args = args;       // command line parameters
        this.prepareOptions();  // initializes available options

        log.exit();
    }

    /**
     * Prepares the {@link #cmdLineOptions} member by adding the single command line options to it.
     * Will be used by the Commons CLI library for parsing command line options.
     */
    protected void prepareOptions() {
        log.entry();

        // first, add all default options
        log.debug("collecting command line options ...");

        // go through all the loaded extensions
        this.commandLineOptionsMap = DBIEP.getInstance()
                .getPluginManager()
                .getExtensions(CommandLineOptionsProvider.class)    // retrieve extensions
                .stream()
                    .map(CommandLineOptionsProvider::get)           // get options from extensions
                    .flatMap(List::stream)                          // flatten all lists
                    .collect(Collectors.toMap(c -> {
                            log.debug("creating commandline option '{}' with values '{}' ...", c.toString(), c);
                            Option o = Option.builder(c.shortName())
                                    .longOpt(c.longName())
                                    .required(c.required())
                                    .hasArg(c.hasArg())
                                    .argName(c.argName())
                                    .desc(c.description())
                                    .type(c.type())
                                    .build();
                            getCmdLineOptions().addOption(o);
                            return o;
                        },Function.identity()));

        log.exit();
    }

    public Configuration parse() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.entry();

        CommandLineParser cmdLineParser = new DefaultParser();
        Configuration configuration;

        CommandLine cmdLine = cmdLineParser.parse(getCmdLineOptions(), getArgs());
        configuration = convertCommandLine2Configuration(new Configuration(), cmdLine);

        log.exit(configuration);
        return configuration;
    }

    /**
     * Helper method for converting options passed on the command line to property values
     * @param cmdLine {@link CommandLine} object containing the options passed
     * @return
     * @throws UnrecognizedOptionException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected Configuration convertCommandLine2Configuration(final Configuration configuration, final CommandLine cmdLine)
            throws UnrecognizedOptionException,
                   InvocationTargetException,
                   NoSuchMethodException,
                   InstantiationException,
                   IllegalAccessException {
        log.entry(cmdLine);

        // iterate over all available options
        Iterator<Option> iterator = cmdLine.iterator();
        while(iterator.hasNext()) {
            Option option = iterator.next();

            // try to find an enumeration member with additional information
            CommandLineOption<?> commandLineOption = getCommandLineOptionsMap().get(option);

            if (null == commandLineOption) {
                final String msg = "Unknown option: -" + option.getOpt();
                log.error(msg);
                throw new UnrecognizedOptionException(msg);
            }

            // finally, set the configuration value
            commandLineOption
                    .configurationSetter()
                    .apply(option.getValue(),
                            commandLineOption,
                            configuration);
        }

        log.exit(configuration);
        return configuration;
    }


}
