package io.github.saigu.log.level.sw.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/27
 */
public class SwitchObjectMapperHolder {
    private static final ObjectMapper SWITCH_OBJECT_MAPPER = new ObjectMapper();

    static {
        SWITCH_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getSwitchObjectMapper() {
        return SWITCH_OBJECT_MAPPER;
    }
}
