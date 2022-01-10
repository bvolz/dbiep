package de.bv.utils.dbiep.tfanalyzer.cmdline;

import de.bv.utils.dbiep.cmdline.CommandLineOptionsParser;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOption;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOptionBuilder;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOptionsProvider;
import lombok.extern.slf4j.XSlf4j;
import org.pf4j.Extension;

import java.util.List;

@Extension
@XSlf4j
public class TFAnalyzerCommandLineOptionsProvider implements CommandLineOptionsProvider {

    private final List<CommandLineOption<?>> options = CommandLineOptionBuilder.getInstance()
            .booleanOption(
                    TFAnalyzerCommandLineOptions.COLUMN_HEADING.getShortName(),
                    TFAnalyzerCommandLineOptions.COLUMN_HEADING.getLongName(),
                    "if specified, input files contain the names of the columns within the first line of data",
                    false,
                    (boolean) TFAnalyzerCommandLineOptions.COLUMN_HEADING.getDefaultValue(),
                    (v, o, c) -> c.add(TFAnalyzerCommandLineOptions.COLUMN_HEADING.getLongName(), Boolean.parseBoolean(v))
                    )
            .options();

    @Override
    public List<CommandLineOption<?>> get() {
        return this.options;
    }
}
