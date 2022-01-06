package de.bv.utils.dbiep.tfanalyzer.cmdline;

import de.bv.utils.dbiep.tfanalyzer.TFAnalyzerConstants;
import de.bv.utils.dbiep.tfanalyzer.model.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.cli.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

@XSlf4j
public class CommandLineOptionsParser {
    @Getter(AccessLevel.PROTECTED)
    private final String[] args;

    @Getter(AccessLevel.PROTECTED)
    private final Options cmdLineOptions = new Options();

    /**
     * Constructs a new object from given command line arguments.
     * @param args array of {@code String} values representing the command line parameters
     */
    public CommandLineOptionsParser(String[] args) {
        log.entry((Object[]) args);

        this.args = args;  // command line parameters
        this.prepareOptions();  // initializes available options from the enumeration containing their definition

        log.exit();
    }

    /**
     * Prepares the {@link #cmdLineOptions} member by adding the single command line options to it.
     * Will be used by the Commons CLI library for parsing command line options.
     */
    protected void prepareOptions() {
        log.entry();

        Arrays.stream(CommandLineOptions.class.getEnumConstants())
                .forEach(o -> {
                    log.debug("creating commandline option '{}' with values '{}' ...", o.toString(), o.getStringRepresentation());

                    getCmdLineOptions().addOption(Option.builder(o.getShortName())
                            .longOpt(o.getLongName())
                            .required(o.isRequired())
                            .hasArg(o.isHasArg())
                            .argName(o.getArgName())
                            .desc(o.getDescription())
                            .type(o.getWrapperClass())
                            .build()
                    );
                });

        log.exit();
    }

    /**
     * Retrieves an object of type {@link Configuration} from the commandline options stored within {@link #args}.
     * If an error is encountered (e.g. an unknown option is found), a message on how to use this tool is printed to
     * stdout and the program is terminated.
     *
     * @return {@code Configuration} object or {@code null}, if an error happened.
     */
    public Configuration getConfiguration() {
        log.entry();

        CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine cmdLine;
        Configuration configuration = null;

        try {
            cmdLine = cmdLineParser.parse(getCmdLineOptions(), getArgs());
            configuration = convertCommandLine2Configuration(cmdLine);
        } catch (Exception e) {
            // an error happend: print usage information and terminate program
            log.catching(e);

            printUsage();               // print usage
            log.exit();
            System.exit(-1);     // terminate program
        }

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
    protected Configuration convertCommandLine2Configuration(final CommandLine cmdLine)
            throws UnrecognizedOptionException,
                   InvocationTargetException,
                   NoSuchMethodException,
                   InstantiationException,
                   IllegalAccessException {
        log.entry(cmdLine);

        Configuration configuration = new Configuration();

        // iterate over all available options
        Iterator<Option> iterator = cmdLine.iterator();
        while(iterator.hasNext()) {
            Option option = iterator.next();

            // try to find an enumeration member with additional information
            Optional<CommandLineOptions> cmdLineOptionOpt = CommandLineOptions.fromShortName(option.getOpt());

            if (!cmdLineOptionOpt.isPresent()) {
                final String msg = "Unknown option: -" + option.getOpt();
                log.error(msg);
                throw new UnrecognizedOptionException(msg);
            }

            CommandLineOptions cmdLineOption = cmdLineOptionOpt.get();
            cmdLineOption.getConfigurationSetter().apply(
                    option.getValue(),
                    cmdLineOption,
                    configuration
            );
        }

        log.exit(configuration);
        return configuration;
    }

    /**
     * Prints information about command line parameters etc. to stdout
     */
    public void printUsage() {
        System.out.printf("TextFile Analyzer %s\n", TFAnalyzerConstants.PROJECT_VERSION);
        System.out.println("----------------------------------------------------------------------------------\n");

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(null);
        helpFormatter.setWidth(80);
        helpFormatter.printHelp("dbiep", getCmdLineOptions());
    }
}
