package com.zjy.service.configuration;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zjy.baseframework.interfaces.IBaseEnum;
import com.zjy.service.common.EnumUtils;
import com.zjy.service.common.EnumSerializer;
import com.zjy.service.common.JacksonDateSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigInteger;
import java.util.Date;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = builder.serializerByType(BigInteger.class, ToStringSerializer.instance)
                    .serializerByType(Long.TYPE, ToStringSerializer.instance)
                    .serializerByType(Long.class, ToStringSerializer.instance)
                    .serializerByType(Date.class, new JacksonDateSerializer());

            for (Class<IBaseEnum> enumClass : EnumUtils.getEnumList()) {
                jackson2ObjectMapperBuilder.serializerByType(enumClass, new EnumSerializer());
            }

            jackson2ObjectMapperBuilder
//                        .serializerByType(LocalDate.class, new LocalDateSerializer())
//                        .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer())
                    .deserializerByType(String.class, new StringDeserializer())
//                        .deserializerByType(Date.class, new DateDeserializer())
//                        .deserializerByType(LocalDate.class, new LocalDateDeserializer())
//                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer())
                    .failOnEmptyBeans(false)
                    .failOnUnknownProperties(false);
        };
    }
}