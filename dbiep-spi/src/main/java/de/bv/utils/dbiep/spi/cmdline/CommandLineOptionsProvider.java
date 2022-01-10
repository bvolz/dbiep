package de.bv.utils.dbiep.spi.cmdline;

import org.pf4j.ExtensionPoint;

import java.util.List;

public interface CommandLineOptionsProvider extends ExtensionPoint {

    List<CommandLineOption<?>> get();

}
