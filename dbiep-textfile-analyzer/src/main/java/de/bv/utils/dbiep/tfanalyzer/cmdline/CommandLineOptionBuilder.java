package de.bv.utils.dbiep.tfanalyzer.cmdline;

import de.bv.utils.dbiep.tfanalyzer.util.TFAnalyzerUtils;

/**
 * Builder class for setting up single command line options.
 *
 * @since 12-2020
 * @version 01-2020
 * @author Bernhard Volz
 */
public final class CommandLineOptionBuilder {

    public static CommandLineOption<Boolean> createBooleanOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final boolean defaultValue
    ) {
        return new CommandLineOption<Boolean>()
                .setType(Boolean.class)
                .setArgName(shortName)
                .setLongName(longName)
                .setDescription(description)
                .setDefaultValue(defaultValue)
                .setRequired(required);
    }

    public static CommandLineOption<Boolean> createBooleanOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required
    ) {
        return new CommandLineOption<Boolean>()
                .setType(Boolean.class)
                .setArgName(shortName)
                .setLongName(longName)
                .setDescription(description)
                .setDefaultValue(false)
                .setRequired(required);
    }

    public static CommandLineOption<String> createStringOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final String defaultValue
    ) {
        return new CommandLineOption<String>()
                .setType(String.class)
                .setArgName(shortName)
                .setLongName(longName)
                .setDescription(description)
                .setDefaultValue(false)
                .setRequired(required)
                .setHasArg(true)
                .setArgName(argName)
                .setDefaultValue(defaultValue);
    }

    public static CommandLineOption<Integer> createIntegerOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final Integer defaultValue
    ) {
        return new CommandLineOption<Integer>()
                .setType(Integer.class)
                .setArgName(shortName)
                .setLongName(longName)
                .setDescription(description)
                .setDefaultValue(false)
                .setRequired(required)
                .setHasArg(true)
                .setArgName(argName)
                .setDefaultValue(defaultValue)
                .setValueFromString(Integer::parseInt)
                .setValueToString(Object::toString);
    }

    public static CommandLineOption<Long> createLongOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final Long defaultValue
    ) {
        return new CommandLineOption<Long>()
                .setType(Long.class)
                .setArgName(shortName)
                .setLongName(longName)
                .setDescription(description)
                .setDefaultValue(false)
                .setRequired(required)
                .setHasArg(true)
                .setArgName(argName)
                .setDefaultValue(defaultValue)
                .setValueFromString(Long::parseLong)
                .setValueToString(Object::toString);
    }

    public static CommandLineOption<Character> createCharacterOption(
            final String shortName,
            final String longName,
            final String description,
            final boolean required,
            final String argName,
            final Character defaultValue
    ) {
        return new CommandLineOption<Character>()
                .setType(Character.class)
                .setArgName(shortName)
                .setLongName(longName)
                .setDescription(description)
                .setDefaultValue(false)
                .setRequired(required)
                .setHasArg(true)
                .setArgName(argName)
                .setDefaultValue(defaultValue)
                .setValueFromString(TFAnalyzerUtils::convertString2Character)
                .setValueToString(Object::toString);
    }

}

