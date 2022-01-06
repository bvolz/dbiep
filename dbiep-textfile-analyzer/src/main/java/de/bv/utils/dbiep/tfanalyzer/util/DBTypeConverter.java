package de.bv.utils.dbiep.tfanalyzer.util;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.util.Map;

public class DBTypeConverter {

    public static class DBTypeConverter_Integer2String extends StdConverter<Integer, String> {

        @Override
        public String convert(Integer integer) {
            return dbType2Text(integer);
        }
    }

    public static class DBTypeConverter_String2Integer extends StdConverter<String, Integer> {

        @Override
        public Integer convert(String s) {
            return text2DBType(s);
        }
    }

    private static final Map<Integer, String> mapping;

    static {
        mapping = Maps.newHashMap();

        for (Field fld : Types.class.getDeclaredFields()) {
            if (Modifier.isStatic(fld.getModifiers()) && Modifier.isFinal(fld.getModifiers()) && fld.getType().equals(int.class)) {

                try {
                    mapping.put(
                            fld.getInt(null),
                            fld.getName()
                    );
                } catch (IllegalAccessException ignored) {}
            }
        }
    }


    public static String dbType2Text(final int dbType) {
        return mapping.get(dbType);
    }

    public static int text2DBType(final String dbType) {
        for (Map.Entry<Integer, String> e : mapping.entrySet()) {
            if (e.getValue().equals(dbType)) {
                return e.getKey();
            }
        }

        throw new IllegalArgumentException("Unknown SQL data type '" + dbType + "'!");
    }
}
