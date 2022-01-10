package de.bv.utils.dbiep.tfanalyzer;

import de.bv.utils.dbiep.DBIEP;
import de.bv.utils.dbiep.tfanalyzer.TFAnalyzer;
import de.bv.utils.dbiep.tfanalyzer.TFAnalyzerBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Executable;

public class CommandLineOptionsTest extends TFAnalyzerBaseTest {

    @Test
    public void testDryRun_long() {
        TFAnalyzer.main(new String[] { "--dry" });

        Assertions.assertTrue(DBIEP.getInstance().getConfiguration().dryRun(), "dry-run should be true");
    }

    @Test
    public void testDryRun_short() {
        TFAnalyzer.main(new String[] { "-d" });

        Assertions.assertTrue(DBIEP.getInstance().getConfiguration().dryRun(), "dry-run should be true");
    }

    /*
    @Test
    public void testWriteConfig_short() {
        TFAnalyzer.main(new String[] {"-wc", cfgFile});
        Assertions.assertAll(
                () -> Assertions.assertTrue(TFAnalyzer.getConfiguration().isWriteCfg(), "writeCfg should be true"),
                () -> Assertions.assertTrue(configFileExists(), "configuration file was not created")
        );
    }

    @Test
    public void testWriteConfig_long() {
        TFAnalyzer.main(new String[] {"--writeCfg", cfgFile});
        Assertions.assertAll(
                () -> Assertions.assertTrue(TFAnalyzer.getConfiguration().isWriteCfg(), "writeCfg should be true"),
                () -> Assertions.assertTrue(configFileExists(), "configuration file was not created")
        );
    }

     */

    private boolean configFileExists() {
        File file = new File(cfgFile);
        return file.exists();
    }

}
