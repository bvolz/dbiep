package de.bv.utils.dbiep.tfanalyzer.simple;

import com.google.common.collect.Lists;
import de.bv.utils.dbiep.DBIEP;
import de.bv.utils.dbiep.spi.utils.ListWrapper;
import de.bv.utils.dbiep.tfanalyzer.TFAnalyzer;
import de.bv.utils.dbiep.tfanalyzer.TFAnalyzerBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Simple1Tests extends TFAnalyzerBaseTest {

    private final ListWrapper<String> baseArgs = ListWrapper.wrap(Lists.newArrayList());

    @BeforeEach
    public void setUp() {
        baseArgs.clear()
                .add("-i")
                .add(basePath + "/csv/simple1")
                .add("-wc")
                .add(cfgFile);
    }

    @Test
    public void testSimple1() {
        TFAnalyzer.main(baseArgs
                .add("-headings")
                .toStringArray()
        );

        // check, if configuration was properly set
      /*  with(DBIEP.getInstance().getConfiguration(), cfg -> assertAll(
                () -> assertTrue(cfg.isHeader(), "heading configuration option not properly set!"),
                () -> assertEquals(1, cfg.getFileConfigurations().size(), "Internal error: there is only a single file present in the directory!"),
                () -> assertEquals("simple1", cfg.getFileConfigurations().get(0).getFilename(), "Internal error: file should be namend 'simple1'"),
                () -> assertEquals(2, cfg.getFileConfigurations().get(0).getColumns().size(), "Number of columns within the file is different")
        ));*/
    }
}
