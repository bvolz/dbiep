package de.bv.utils.dbiep.tfanalyzer;

import de.bv.utils.dbiep.tfanalyzer.cmdline.CommandLineOptionsParser;
import de.bv.utils.dbiep.tfanalyzer.model.Configuration;
import de.bv.utils.dbiep.tfanalyzer.model.ModelSerializer;
import de.bv.utils.dbiep.tfanalyzer.processor.DirectoryProcessor;

import java.io.IOException;

public class TFAnalyzer {
    private static Configuration configuration;

    public static void main(String[] args) {
        CommandLineOptionsParser cmdLineParser = new CommandLineOptionsParser(args);
        configuration = cmdLineParser.getConfiguration();

        try {
            parseInput(configuration);

            ModelSerializer modelSerializer = new ModelSerializer();
            if (configuration.isWriteCfg()) {
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
    }
}
