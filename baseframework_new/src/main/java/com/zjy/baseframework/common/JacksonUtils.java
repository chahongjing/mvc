package com.zjy.baseframework.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

class JacksonUtils {
    private static ObjectMapper MAPPER = new ObjectMapper();

    private JacksonUtils() {}

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    }

    public static String toJSON(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception var2) {
            throw new IllegalArgumentException(var2.getMessage());
        }
    }
}
