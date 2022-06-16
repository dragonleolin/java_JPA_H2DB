package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper OBJ_MAPPER = new ObjectMapper();

    public static String convertObjectToJsonString(Object pojo) {
        try {
            String jsonString = getObjectMapper()
                    .writeValueAsString(pojo);
            return jsonString;
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception occurred when convert object to json string, Error stack: ", e);
            throw new RuntimeException("Exception occurred when convert object to json string");
        }
    }

    public static ObjectMapper getObjectMapper() {
        return OBJ_MAPPER;
    }
}
