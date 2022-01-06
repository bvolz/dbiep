package de.bv.utils.dbiep.tfanalyzer.cmdline;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.function.Function;

/**
 * Represents a single option on the command line.
 *
 * @param <T> type of the value of the option
 *
 * @version 2022-01
 * @since 2022-01
 * @author Bernhard Volz - initial implementation
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommandLineOption<T> {

    private String shortName;
    private String longName;
    private Object defaultValue;
    private boolean required = false;

    private String description;
    private boolean hasArg = false;
    private String argName;

    private Class<?> type;

    private Function<String, T> valueFromString;
    private Function<T, String> valueToString;
}
