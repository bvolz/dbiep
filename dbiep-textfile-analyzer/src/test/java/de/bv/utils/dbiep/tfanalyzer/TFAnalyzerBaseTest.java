package de.bv.utils.dbiep.tfanalyzer;

import de.bv.utils.dbiep.tfanalyzer.simple.Simple1Tests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.net.URL;
import java.util.function.Consumer;

public class TFAnalyzerBaseTest {

    protected static String cfgFile;

    @BeforeAll
    public static void setup() {
        URL csvData = TFAnalyzerBaseTest.class.getResource("/");
        cfgFile = (csvData != null ? csvData.getPath() : ".") + "/testConfig.json";
    }

    @AfterEach
    public void clearConfig() {
        File testConfig = new File(cfgFile);
        if (testConfig.exists()) {
            testConfig.delete();
        }
    }

    protected static <T> T with(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }
}
