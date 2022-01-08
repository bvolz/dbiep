package de.bv.utils.dbiep.tfanalyzer.simple;

import de.bv.utils.dbiep.tfanalyzer.TFAnalyzer;
import de.bv.utils.dbiep.tfanalyzer.TFAnalyzerBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandLineOptionsTest extends TFAnalyzerBaseTest {

    @Test
    public void testDryRun_long() {
        TFAnalyzer.main(new String[] { "--dry" });

        Assertions.assertTrue(TFAnalyzer.getConfiguration().isDryRun(), "dry-run should be true");
    }

    @Test
    public void testDryRun_short() {
        TFAnalyzer.main(new String[] { "-d" });

        Assertions.assertTrue(TFAnalyzer.getConfiguration().isDryRun(), "dry-run should be true");
    }

}
