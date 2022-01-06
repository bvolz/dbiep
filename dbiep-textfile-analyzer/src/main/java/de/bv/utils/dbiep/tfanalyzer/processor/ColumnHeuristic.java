package de.bv.utils.dbiep.tfanalyzer.processor;

import de.bv.utils.dbiep.tfanalyzer.model.Configuration;
import de.bv.utils.dbiep.tfanalyzer.model.FileConfiguration;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.XSlf4j;

import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Accessors(chain = true)
@XSlf4j
public class ColumnHeuristic {

    @Data
    @Accessors(chain = true)
    public static class ColumnInfo {

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private int id;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private boolean integerValue;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private boolean doubleValue;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private boolean dateValue;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private boolean textValue;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private boolean booleanValue;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private boolean empty;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private long minInteger;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private long maxInteger;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private double minDouble;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private double maxDouble;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private Date minDate;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private Date maxDate;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private long maxEntryLength;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private long minEntryLength;

        @Getter(onMethod_={@Synchronized}) @Setter(onMethod_={@Synchronized})
        private int mappedType;

        @Synchronized
        void updateInteger(final long value) {
            setIntegerValue(true);

            if (value < minInteger) {
                minInteger = value;
            } else if (value > maxInteger) {
                maxInteger = value;
            }
        }

        @Synchronized
        void updateDouble(final double value) {
            setDoubleValue(true);

            if (value < minDouble) {
                minDouble = value;
            } else if (value > maxDouble) {
                maxDouble = value;
            }
        }

        @Synchronized
        public boolean isEmptyColumn() {
            return !isTextValue() && !isDateValue() && !isDoubleValue() && !isIntegerValue() && !isBooleanValue() && isEmpty();
        }
    }

    public final static String REGEX_BOOLEAN_TRUE = "^true$";
    public final static String REGEX_BOOLEAN_FALSE = "^false$";

    @Getter
    private ColumnInfo[] columns;

    @Getter
    private final String dateFormat;

    /**
     * Contains the number of lines processed so far.
     */
    @Getter
    private long linesProcessed;

    @Getter
    private final Configuration configuration;

    @Getter
    private final FileConfiguration csvFileCfg;

    public ColumnHeuristic(final FileConfiguration csvFileCfg, final Configuration configuration) {
        int numberOfColumns = csvFileCfg.getColumns().size();
        this.columns = new ColumnInfo[numberOfColumns];
        this.columns = IntStream.range(0, numberOfColumns)
                .boxed()
                .map(i -> new ColumnInfo().setId(i))
                .collect(Collectors.toList())
                .toArray(getColumns());

        this.dateFormat = configuration.getDateFormat();
        this.linesProcessed = csvFileCfg.getLinesToSkip() + 1;
        this.configuration = configuration;
        this.csvFileCfg = csvFileCfg;
    }

    public ColumnHeuristic process(final String[] columns) {
        // increment lines counter
        this.linesProcessed += 1;

        int n = 0;
        for (String value : columns) {

            // check: current index should not exceed the number of columns!
            if (n == getColumns().length) {
                String errorMsg = String.format("%s.%s [line %d]: additional column found -> values: %s",
                        getCsvFileCfg().getFilename(),
                        getConfiguration().getFileExtension(),
                        getLinesProcessed(),
                        Arrays.toString(columns)
                );
                log.warn(errorMsg);

                if (configuration.isPermissiveAdditionalColumns()) {
                    continue;
                }

                String exceptionMsg = String.format("Error during parsing file %s.%s: in line %d an additional column value was found! The analysis so far showed less columns present. Aborting further processing ...",
                        getCsvFileCfg().getFilename(),
                        getConfiguration().getFileExtension(),
                        getLinesProcessed()
                );
                throw new de.bv.utils.dbiep.tfanalyzer.exceptions.ParseException(exceptionMsg);
            }

            ColumnInfo columnInfo = getColumns()[n++];

            // update length
            int length = null != value ? value.length() : 0;
            if (columnInfo.getMaxEntryLength() < length) {
                columnInfo.setMaxEntryLength(length);
            }
            if (columnInfo.getMinEntryLength() > length) {
                columnInfo.setMinEntryLength(length);
            }

            // handle empty values
            if (length == 0) {
                columnInfo.setEmpty(true);
                continue;
            }

            // lowercase value, trimmed
            final String lValue = value.toLowerCase().trim();

            // test different types
            if (isBoolean(lValue)) {
                columnInfo.setBooleanValue(true);
            } else if (isInteger(value)) {
                columnInfo.updateInteger(Long.parseLong(value));
            } else if (isDouble(value)) {
                columnInfo.updateDouble(Double.parseDouble(value));
            } else if (isDate(value)) {
                columnInfo.setDateValue(true);
            } else {
                columnInfo.setTextValue(true);
            }

        }

        return this;
    }

    public ColumnHeuristic finishProcessing() {
        // iterate over column information and determine type
        Arrays.stream(getColumns())
                .forEach(c -> {
                    // check for boolean
                    if (c.isBooleanValue() && !c.isIntegerValue() && !c.isDoubleValue() && !c.isDateValue() && !c.isTextValue()) {
                        c.setMappedType(Types.BOOLEAN);
                    } else if (c.isIntegerValue() && !c.isDoubleValue() && !c.isTextValue() && !c.isDateValue()) {
                        // determine range
                        if (0 <= c.getMinInteger() && 255 >= c.getMaxInteger()) {
                            c.setMappedType(Types.TINYINT);
                        } else if (Byte.MIN_VALUE <= c.getMinInteger() && Byte.MAX_VALUE >= c.getMaxInteger()) {
                            c.setMappedType(Types.SMALLINT);
                        } else if (Short.MIN_VALUE <= c.getMinInteger() && Short.MAX_VALUE >= c.getMaxInteger()) {
                            c.setMappedType(Types.SMALLINT);
                        } else if (Integer.MIN_VALUE <= c.getMinInteger() && Integer.MAX_VALUE >= c.getMaxInteger()) {
                            c.setMappedType(Types.INTEGER);
                        } else {
                            c.setMappedType(Types.BIGINT);
                        }
                    } else if (c.isDoubleValue() && !c.isTextValue() && !c.isDateValue()) {
                        // determine range
                        if (-Float.MAX_VALUE <= c.getMinDouble() && Float.MAX_VALUE >= c.getMaxDouble()) {
                            c.setMappedType(Types.FLOAT);
                        } else if (-Double.MAX_VALUE <= c.getMinDouble() && Double.MAX_VALUE >= c.getMaxDouble()) {
                            c.setMappedType(Types.DOUBLE);
                        } else {
                            System.exit(-1);
                            // TODO: Error, unhandled float range!
                        }
                    } else if (c.isDateValue() && !c.isTextValue()) {
                        c.setMappedType(Types.TIMESTAMP);
                    } else if (c.isTextValue()) {
                        /*
                        c.setMappedType(new StringBuilder(Types.NVARCHAR)
                                .append("(")
                                .append(c.getMaxEntryLength() > 4000 ? "MAX" : Long.toString(c.getMaxEntryLength()))
                                .toString()
                        );*/
                        c.setMappedType(Types.NVARCHAR);
                        c.setMaxEntryLength(c.getMaxEntryLength() + 5); // TODO: increase the column size a bit
                    } else if (c.isEmpty()) {
                        c.setMappedType(Types.NVARCHAR);
                        c.setMaxEntryLength(255);
                    } else {
                        c.setMappedType(Types.NVARCHAR);
                        c.setMaxEntryLength(255);
                    }
                });

        return this;
    }


    protected boolean isInteger(final String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    protected boolean isDouble(final String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    protected boolean isDate(final String value) {
        DateFormat dateFormat = new SimpleDateFormat(getDateFormat());
        try {
            dateFormat.parse(value);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    protected boolean isBoolean(final String value) {
        return value.matches(REGEX_BOOLEAN_TRUE) || value.matches(REGEX_BOOLEAN_FALSE);
    }
}

