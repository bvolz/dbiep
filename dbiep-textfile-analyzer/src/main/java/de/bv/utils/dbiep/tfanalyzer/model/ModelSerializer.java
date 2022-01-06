package de.bv.utils.dbiep.tfanalyzer.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ModelSerializer {
    @Getter
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public void serialize(final String filename, final Configuration configuration) throws IOException {
        try {
            getObjectMapper().writeValue(
                    new File(filename),
                    configuration.getFileConfigurations()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void serialize(final Configuration configuration) throws IOException {
        try {
            getObjectMapper().writeValue(
                    System.out,
                    configuration.getFileConfigurations()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deserialize(final String filename, final Configuration configuration) throws IOException {
        try {
            List<FileConfiguration> csvFiles = getObjectMapper().readValue(
                    new File(filename),
                    new TypeReference<List<FileConfiguration>>() {}
            );

            configuration.getFileConfigurations().clear();
            configuration.getFileConfigurations().addAll(csvFiles);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        // post process: add path
        configuration.getFileConfigurations()
                .forEach(f -> f.setPath(configuration.getDirectory() + File.separator + f.getFilename() + "." + configuration.getFileExtension()));
    }

}
