package de.bv.utils.dbiep;

import de.bv.utils.dbiep.cmdline.CommandLineOptionsParser;
import de.bv.utils.dbiep.spi.configuration.Configuration;
import de.bv.utils.dbiep.tfanalyzer.DBIEPConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.lang.reflect.InvocationTargetException;

@XSlf4j
public abstract class DBIEP {

    @Accessors @Getter
    private final PluginManager pluginManager;

    @Accessors @Getter @Setter
    private Configuration configuration;

    @Accessors @Getter
    private CommandLineOptionsParser commandLineOptionsParser;

    protected DBIEP(final String[] args) {
        this.pluginManager = new DefaultPluginManager();
        instance = this;
        this.initialize(args);
    }

    protected abstract void dbiepMain();

    @Getter
    private static DBIEP instance;

    protected void initialize(String[] args) {
        this.commandLineOptionsParser = new CommandLineOptionsParser(args); // create parser

        try {
            parseCommandLine(args);
        } catch (ParseException e) {
            log.catching(e);
            System.err.println(e.getMessage());
            printUsage();
        }
    }

    protected void parseCommandLine(final String[] args) throws ParseException {
        try {
            this.configuration = this.getCommandLineOptionsParser().parse();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            log.catching(e);
            log.error("Internal error, exiting ...");
            System.exit(-1);
        }
    }

    protected abstract void printUsageHeader();
    protected abstract String getCommandLineSyntax();

    /**
     * Prints information about command line parameters etc. to stdout
     */
    protected void printUsage() {
        printUsageHeader();

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(null);
        helpFormatter.setWidth(80);
        helpFormatter.printHelp(getCommandLineSyntax(), getCommandLineOptionsParser().getCmdLineOptions());
    }


}
