package de.bv.utils.dbiep.spi.utils;

public class DBIEPUtils {
    public static char convertString2Character(final String value) {
        switch (value) {
            case "\\t":
                return '\t';
            case "\\n":
                return '\n';
            case "\\":
                return '\\';
        }

        return value.charAt(0);
    }
}
