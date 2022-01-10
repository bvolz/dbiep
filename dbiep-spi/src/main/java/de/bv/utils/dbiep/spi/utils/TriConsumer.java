package de.bv.utils.dbiep.spi.utils;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void apply(T t, U u, V v) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
