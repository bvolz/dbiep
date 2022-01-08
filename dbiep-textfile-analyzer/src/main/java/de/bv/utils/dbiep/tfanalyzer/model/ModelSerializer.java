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
                    configuration
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
                    configuration
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Configuration deserialize(final String filename) throws IOException {
        Configuration configuration;
        try {
             configuration = getObjectMapper().readValue(
                    new File(filename),
                    new TypeReference<Configuration>() {}
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        // post process: add path
        configuration.getFileConfigurations()
                .forEach(f -> f.setPath(configuration.getDirectory() + File.separator + f.getFilename() + "." + configuration.getFileExtension()));

        return configuration;
    }

}
