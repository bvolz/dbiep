package de.bv.utils.dbiep.tfanalyzer;

import de.bv.utils.dbiep.tfanalyzer.simple.Simple1Tests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.net.URL;
import java.util.function.Consumer;

public class TFAnalyzerBaseTest {

    protected static String cfgFile;
    protected static String basePath;

    @BeforeAll
    public static void setup() {
        URL csvData = TFAnalyzerBaseTest.class.getResource("/");
        basePath = csvData != null ? csvData.getPath() : ".";
        cfgFile = basePath + "/testConfig.json";
    }

    @AfterEach
    public void clearConfig() {
        File testConfig = new File(cfgFile);
        if (testConfig.exists()) {
            Assertions.assertTrue(testConfig.delete(), "could not delete generated configuration");
        }
    }

    @SuppressWarnings({"UnusedReturnValue"})
    protected static <T> T with(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }
}
