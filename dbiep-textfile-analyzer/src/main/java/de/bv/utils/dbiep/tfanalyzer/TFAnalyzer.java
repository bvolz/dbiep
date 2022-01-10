package de.bv.utils.dbiep.tfanalyzer;

import de.bv.utils.dbiep.DBIEP;

public class TFAnalyzer extends DBIEP {

    protected TFAnalyzer(String[] args) {
        super(args);
    }

    @Override
    protected void dbiepMain() {

    }

    public static void main(String[] args) {
        new TFAnalyzer(args).dbiepMain();
    }

    @Override
    protected void printUsageHeader() {
        System.out.printf("DBIEP TextFile Analyzer %s\n", de.bv.utils.dbiep.tfanalyzer.DBIEPConstants.PROJECT_VERSION);
        System.out.println("----------------------------------------------------------------------------------\n");
    }

    @Override
    protected String getCommandLineSyntax() {
        return "tfanalyzer";
    }

    /*
    private static Configuration configuration;

    public static void main(String[] args) {
        CommandLineOptionsParser cmdLineParser = new CommandLineOptionsParser(args);
        configuration = cmdLineParser.getConfiguration();

        try {
            parseInput(configuration);

            ModelSerializer modelSerializer = new ModelSerializer();
            if (configuration.isWriteCfg() && !configuration.isDryRun()) {
                modelSerializer.serialize(configuration.getCfgFile(), configuration);
            } else {
                modelSerializer.serialize(configuration);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseInput(final Configuration configuration) throws IOException {
        DirectoryProcessor directoryProcessor = new DirectoryProcessor()
                .init(configuration)
                .process()
        ;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }*/
}
