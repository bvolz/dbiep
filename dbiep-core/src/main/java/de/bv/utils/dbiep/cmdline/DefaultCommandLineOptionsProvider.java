package de.bv.utils.dbiep.cmdline;

import de.bv.utils.dbiep.spi.cmdline.CommandLineOption;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOptionBuilder;
import de.bv.utils.dbiep.spi.cmdline.CommandLineOptionsProvider;
import org.pf4j.Extension;

import java.util.List;

@Extension
public class DefaultCommandLineOptionsProvider implements CommandLineOptionsProvider {

    private final List<CommandLineOption<?>> defaultOptions = CommandLineOptionBuilder.getInstance()
            .booleanOption("dry",
                    "dryRun",
                    "if set, no changes will be performed; use for testing purposes",
                    false,
                    false,
                    (v, o, c) -> c.dryRun(true))
            .options();

    @Override
    public List<CommandLineOption<?>> get() {
        return this.defaultOptions;
    }
}
