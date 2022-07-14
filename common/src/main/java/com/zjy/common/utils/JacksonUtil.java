package com.zjy.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zjy.baseframework.interfaces.IBaseEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Component
public class JacksonUtil implements JsonUtils {
    private static ObjectMapper MAPPER;

    public JacksonUtil() {
    }

    public <T> T parse(String json, Class<T> clazz) {
        return parse(json, clazz, (TypeReference<T>) null, (JavaType) null);
    }

    public static <T> T parse(String json, TypeReference<T> type) {
        return parse(json, (Class<T>) null, type, (JavaType) null);
    }

    public static <T> T parse(String json, JavaType type) {
        return (T) parse(json, (Class) null, (TypeReference) null, type);
    }

    public <T> List<T> parseList(String json, Class<T> tClass) {
        CollectionType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, tClass);
        return (List) parse(json, (Class) null, (TypeReference) null, javaType);
    }

    public <T> T[] parseArray(String json, Class<T> tClass) {
        ArrayType arrayType = MAPPER.getTypeFactory().constructArrayType(tClass);
        return (T[]) parse(json, (Class) null, (TypeReference) null, arrayType);
    }

    private static JsonNode getJsonNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception var2) {
            throw new IllegalArgumentException(var2.getMessage());
        }
    }

    public String toJSON(Object obj) {
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
//        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        MAPPER.registerModule(new JavaTimeModule());
//        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        processBuilder(builder);
        MAPPER = builder.build();
    }

    public static void processBuilder(Jackson2ObjectMapperBuilder b) {
        Jackson2ObjectMapperBuilder builder = b.serializerByType(BigInteger.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(BigInteger.class, ToStringSerializer.instance)
                .serializerByType(Date.class, new JacksonDateSerializer())
                .deserializerByType(Date.class, new JacksonDateDeserializer());

        for (Class<IBaseEnum> enumClass : EnumUtils.getEnumList()) {
            builder.serializerByType(enumClass, new EnumSerializer());
        }

        builder
//                        .serializerByType(LocalDate.class, new LocalDateSerializer())
//                        .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer())
                .deserializerByType(String.class, new StringDeserializer())
//                        .deserializerByType(Date.class, new DateDeserializer())
//                        .deserializerByType(LocalDate.class, new LocalDateDeserializer())
//                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer())
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false);

        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.modules(new JavaTimeModule());
//        builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        builder.visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);

        TypeResolverBuilder<?> typeResolver = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL);
        typeResolver = typeResolver.init(JsonTypeInfo.Id.NONE, null);
        typeResolver = typeResolver.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.defaultTyping(typeResolver);
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper om = new ObjectMapper();
//        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        om.registerModule(new JavaTimeModule());
//        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        return om;
//    }
}