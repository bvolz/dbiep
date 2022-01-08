package de.bv.utils.dbiep.tfanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import de.bv.utils.dbiep.tfanalyzer.cmdline.CommandLineOptions;
import lombok.Data;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * Holds the configuration of TFAnalyzer as specified on the command line.
 * Please note, that these are global options which may be of used by every extension.
 *
 * @version 2020-11
 * @since 2020-08
 * @author Bernhard Volz - inital contribution
 */
@Data
public class Configuration {

    /**
     * If set to {@code true}, a dry-run is performed, i.e. no changes are made to the database or to files.
     */
    @JsonIgnore
    private boolean dryRun = false;

    private String cfgFile;
    private boolean writeCfg = false;

    private String directory = System.getProperty("user.dir");
    private String fileExtension = (String) CommandLineOptions.FILE_EXTENSION.getDefaultValue();
    private char separationCharacter = (char) CommandLineOptions.SEPARATOR.getDefaultValue();
    private char quotationCharacter = (char) CommandLineOptions.QUOTATION_CHARACTER.getDefaultValue();
    private char escapeCharacter = (char) CommandLineOptions.ESCAPE_CHARACTER.getDefaultValue();

    private Charset charset = Charset.forName((String) CommandLineOptions.CHARSET.getDefaultValue());

    private int guess = (int) CommandLineOptions.GUESS_MAX.getDefaultValue();
    private boolean header = false;
    private boolean dropEmptyColumns = false;
    //private int linesToSkip = 0;    // TODO

    private String dateFormat = (String) CommandLineOptions.DATE_FORMAT.getDefaultValue();

    private boolean recreateSchema = false;
    private boolean truncateTables = false;

    /**
     * If set to {@code true}, additional column values will be ignored.
     * If set to {@code false} (default value!), additional column values will raise an error
     *
     * @since 2020-11
     */
    private boolean permissiveAdditionalColumns = false;

    private List<FileConfiguration> fileConfigurations = Collections.synchronizedList(Lists.newArrayList());
}
