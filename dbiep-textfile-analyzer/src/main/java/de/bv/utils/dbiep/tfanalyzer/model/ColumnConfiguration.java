package de.bv.utils.dbiep.tfanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.bv.utils.dbiep.tfanalyzer.util.DBTypeConverter;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ColumnConfiguration {
    /**
     * Id number of the column.
     * The id corresponds to the ordinal number of the column within a file.
     *
     * The value will be serialized and deserialized when saving / loading the configuration.
     */
    private int id;

    /**
     * Name of the column.
     *
     * The value will be serialized and deserialized when saving / loading the configuration.
     */
    private String name;

    /**
     * Identifier of the JDBC data type to use for this column.
     * During serialization / deserialization, the numeric identifier will be converted into human readable constants.
     * The values of these constants are derived from the fields of {@link java.sql.Types}.
     *
     * The value will be serialized and deserialized when saving / loading the configuration.
     */
    @JsonSerialize(converter = DBTypeConverter.DBTypeConverter_Integer2String.class)
    @JsonDeserialize(converter = DBTypeConverter.DBTypeConverter_String2Integer.class)
    private int mappedType;

    /**
     * Set to {@code true}, if the column is empty and does not contain any value at all.
     *
     * The value will be serialized and deserialized when saving / loading the configuration.
     */
    private boolean empty;

    /**
     * Indicates, whether the column may contain empty values or not.
     * Will be set to {@code true}, in case there is at least a single empty value found within a column.
     *
     * The value will be serialized and deserialized when saving / loading the configuration.
     */
    private boolean nullable;

    /**
     * Provides information about the max length of text values stored within a column.
     *
     * The value will be serialized and deserialized when saving / loading the configuration.
     */
    private long maxLength;

    /**
     * Contains a link to the configuration settings of the csv file this column is stored with.
     *
     * The value will <b>NOT</b> be serialized and deserialized when saving / loading the configuration.
     */
    @ToString.Exclude
    @JsonIgnore
    private FileConfiguration csvFile;

    /**
     * Creates a summary of the column configuration as readable text for debug and logging outputs.
     *
     * @return String with the summary
     */
    @JsonIgnore
    public String getSummary() {
        return  name + "(maxLen = " + maxLength + ")";
    }
}
