package de.bv.utils.dbiep.tfanalyzer.util;

public final class TFAnalyzerUtils {
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
