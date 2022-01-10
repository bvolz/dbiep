package de.bv.utils.dbiep.spi.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true, fluent = true)
public class Configuration {

    @JsonIgnore
    private boolean dryRun;

    private Map<String, Object> values = Maps.newHashMap();

    public void add(String name, boolean value) {
        this.values.put(name, value);
    }
}
