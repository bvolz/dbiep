package de.bv.utils.dbiep.tfanalyzer.cmdline;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TFAnalyzerCommandLineOptions {
    COLUMN_HEADING("tf.heading", "textFile.columnHeading", false)

    ;

    @Getter
    private final String shortName;

    @Getter
    private final String longName;

    @Getter
    private final Object defaultValue;
}
