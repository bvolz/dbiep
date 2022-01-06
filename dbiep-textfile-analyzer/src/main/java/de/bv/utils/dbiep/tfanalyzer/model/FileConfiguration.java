package de.bv.utils.dbiep.tfanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *
 * @version 2020-12
 * @since 2020-12
 *
 * @author Bernhard Volz - initial implementation
 */
@Data
@Accessors(chain = true)
public class FileConfiguration {
    @JsonIgnore
    private String path;

    private String filename;

    private boolean headerPresent = false;

    private int linesToSkip = 0;

    private boolean empty = false;

    private List<ColumnConfiguration> columns = Lists.newArrayList();

    @JsonIgnore
    public String getSummary() {
        final StringBuilder stringBuilder = new StringBuilder("file: ").append(filename)
                .append(headerPresent ? ", column headings present" : "")
                .append(", linesToSkip = ").append(linesToSkip)
                .append(", numberOfColumns = ").append(columns.size())
                .append(", columns = {");

        for (int i = 0; i < columns.size(); ++i) {
            stringBuilder.append(i > 0 ? ", " : "")
                    .append(columns.get(i).getSummary());
        }

        return stringBuilder.append("}").toString();
    }
}
