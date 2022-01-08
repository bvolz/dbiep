package de.bv.utils.dbiep.tfanalyzer.simple;

import com.google.common.collect.Lists;
import de.bv.utils.dbiep.tfanalyzer.TFAnalyzer;
import de.bv.utils.dbiep.tfanalyzer.TFAnalyzerBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

public class Simple1Tests extends TFAnalyzerBaseTest {

    private final List<String> baseArgs = Lists.newArrayList();

    @BeforeEach
    public void setUp() {
        baseArgs.clear();

        URL csvData = Simple1Tests.class.getResource("/csv/simple1");
        baseArgs.add("-i");
        baseArgs.add(csvData.getPath());
    }

    @Test
    public void testSimple1() {
        baseArgs.add("-headings");
        TFAnalyzer.main(baseArgs.toArray(new String[0]));

        // check, if configuration was properly set
        with(TFAnalyzer.getConfiguration(), cfg -> {

        });

        Assertions.assertTrue(TFAnalyzer.getConfiguration().isHeader(), "heading configuration option not properly set!");
        Assertions.assertEquals(1, TFAnalyzer.getConfiguration().getFileConfigurations().size(), "Internal error: there is only a single file present in the directory!");
        Assertions.assertEquals("simple1", TFAnalyzer.getConfiguration().getFileConfigurations().get(0).getFilename());
    }
}
