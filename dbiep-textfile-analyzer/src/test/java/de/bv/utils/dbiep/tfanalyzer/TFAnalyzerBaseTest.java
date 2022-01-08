package de.bv.utils.dbiep.tfanalyzer;

import java.util.function.Consumer;

public class TFAnalyzerBaseTest {
    protected static <T> T with(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }
}
