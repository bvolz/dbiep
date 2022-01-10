package de.bv.utils.dbiep.spi.cmdline;

import com.google.common.collect.Lists;
import de.bv.utils.dbiep.spi.configuration.Configuration;
import de.bv.utils.dbiep.spi.utils.DBIEPUtils;
import de.bv.utils.dbiep.spi.utils.TriConsumer;

import java.util.List;

/**
 * Builder class for setting up single command line options.
 *
 * @since 2020-12
 * @version 2022-01
 * @author Bernhard Volz
 */
public final class CommandLineOptionBuilder {

    public static CommandLineOptionBuilder getInstance() {
        return new CommandLineOptionBuilder();
    }

    private final List<CommandLineOption<?>> options = Lists.newArrayList();

    public List<CommandLineOption<?>> options() {
        return this.options;
    }

    public CommandLineOptionBuilder clear() {
        this.options.clear();
        return this;
    }

    public CommandLineOptionBuilder booleanOption(final String shortName,
                                                  final String longName,
                                                  final String description,
                                                  final boolean required,
                                                  final boolean defaultValue,
                                                  final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter) {
        options.add(createBooleanOption(shortName, longName, description, required, defaultValue, configurationSetter));
        return this;
    }

    public static CommandLineOption<Boolean> createBooleanOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final boolean defaultValue,
            final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter
    ) {
        return new CommandLineOption<Boolean>()
                .type(Boolean.class)
                .argName(shortName)
                .longName(longName)
                .description(description)
                .defaultValue(defaultValue)
                .required(required)
                .configurationSetter(configurationSetter);
    }

    public CommandLineOptionBuilder booleanOption(final String shortName,
                                                  final String longName,
                                                  final String description,
                                                  final boolean required,
                                                  final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter) {
        this.options.add(createBooleanOption(shortName, longName, description, required, configurationSetter));
        return this;
    }

    public static CommandLineOption<Boolean> createBooleanOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter
    ) {
        return new CommandLineOption<Boolean>()
                .type(Boolean.class)
                .argName(shortName)
                .longName(longName)
                .description(description)
                .defaultValue(false)
                .required(required)
                .configurationSetter(configurationSetter);
    }

    public CommandLineOptionBuilder stringOption(final String shortName,
                                                 final String longName,
                                                 final String description,
                                                 final boolean required,
                                                 final String argName,
                                                 final String defaultValue,
                                                 final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter) {
        this.options.add(createStringOption(shortName, longName, description, required, argName, defaultValue, configurationSetter));
        return this;
    }

    public static CommandLineOption<String> createStringOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final String defaultValue,
            final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter
    ) {
        return new CommandLineOption<String>()
                .type(String.class)
                .argName(shortName)
                .longName(longName)
                .description(description)
                .defaultValue(false)
                .required(required)
                .hasArg(true)
                .argName(argName)
                .defaultValue(defaultValue)
                .configurationSetter(configurationSetter);
    }

    public CommandLineOptionBuilder integerOption(final String shortName,
                                                  final String longName,
                                                  final String description,
                                                  final boolean required,
                                                  final String argName,
                                                  final Integer defaultValue,
                                                  final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter) {
        this.options.add(createIntegerOption(shortName, longName, description, required, argName, defaultValue, configurationSetter));
        return this;
    }

    public static CommandLineOption<Integer> createIntegerOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final Integer defaultValue,
            final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter
    ) {
        return new CommandLineOption<Integer>()
                .type(Integer.class)
                .argName(shortName)
                .longName(longName)
                .description(description)
                .defaultValue(false)
                .required(required)
                .hasArg(true)
                .argName(argName)
                .defaultValue(defaultValue)
                .valueFromString(Integer::parseInt)
                .valueToString(Object::toString)
                .configurationSetter(configurationSetter);
    }

    public CommandLineOptionBuilder longOption(final String shortName,
                                               final String longName,
                                               final String description,
                                               final boolean required,
                                               final String argName,
                                               final Long defaultValue,
                                               final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter) {
        this.options.add(createLongOption(shortName, longName, description, required, argName, defaultValue, configurationSetter));
        return this;
    }

    public static CommandLineOption<Long> createLongOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final Long defaultValue,
            final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter
    ) {
        return new CommandLineOption<Long>()
                .type(Long.class)
                .argName(shortName)
                .longName(longName)
                .description(description)
                .defaultValue(false)
                .required(required)
                .hasArg(true)
                .argName(argName)
                .defaultValue(defaultValue)
                .valueFromString(Long::parseLong)
                .valueToString(Object::toString)
                .configurationSetter(configurationSetter);
    }

    public CommandLineOptionBuilder characterOption(final String shortName,
                                                    final String longName,
                                                    final String description,
                                                    final boolean required,
                                                    final String argName,
                                                    final Character defaultValue,
                                                    final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter) {
        this.options.add(createCharacterOption(shortName, longName, description, required, argName, defaultValue, configurationSetter));
        return this;
    }

    public static CommandLineOption<Character> createCharacterOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final Character defaultValue,
            final TriConsumer<String, CommandLineOption<?>, Configuration> configurationSetter
    ) {
        return new CommandLineOption<Character>()
                .type(Character.class)
                .argName(shortName)
                .longName(longName)
                .description(description)
                .defaultValue(false)
                .required(required)
                .hasArg(true)
                .argName(argName)
                .defaultValue(defaultValue)
                .valueFromString(DBIEPUtils::convertString2Character)
                .valueToString(Object::toString)
                .configurationSetter(configurationSetter);
    }

}

