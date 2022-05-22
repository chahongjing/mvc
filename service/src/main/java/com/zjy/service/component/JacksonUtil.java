package com.zjy.service.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class JacksonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JacksonUtil() {
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return parse(json, clazz, (TypeReference<T>) null, (JavaType)null);
    }

    public static <T> T parse(String json, TypeReference<T> type) {
        return parse(json, (Class<T>) null, type, (JavaType)null);
    }

    public static <T> T parse(String json, JavaType type) {
        return (T) parse(json, (Class)null, (TypeReference)null, type);
    }

    public static <T> List<T> parseList(String json, Class<T> tClass) {
        CollectionType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, tClass);
        return (List)parse(json, (Class)null, (TypeReference)null, javaType);
    }

    public static <T> T[] parseArray(String json, Class<T> tClass) {
        ArrayType arrayType = MAPPER.getTypeFactory().constructArrayType(tClass);
        return (T[]) parse(json, (Class)null, (TypeReference)null, arrayType);
    }

    public static JsonNode getJsonNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception var2) {
            throw new IllegalArgumentException(var2.getMessage());
        }
    }

    public static String toJSON(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception var2) {
            throw new IllegalArgumentException(var2.getMessage());
        }
    }

    public static <T> T parse(JsonNode jsonNode, TypeReference<T> typeReference) {
        try {
            ObjectReader objectReader = MAPPER.readerFor(typeReference);
            return objectReader.readValue(jsonNode);
        } catch (Exception var3) {
            throw new IllegalArgumentException(var3.getMessage());
        }
    }

    private static <T> T parse(String json, Class<T> clazz, TypeReference<T> typeReference, JavaType type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                if (clazz != null) {
                    return MAPPER.readValue(json, clazz);
                } else if (typeReference != null) {
                    return MAPPER.readValue(json, typeReference);
                } else {
                    return type != null ? MAPPER.readValue(json, type) : null;
                }
            } catch (Exception var5) {
                throw new IllegalArgumentException(var5.getMessage());
            }
        }
    }

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    }
}