package com.zjy.web.config;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zjy.service.component.JacksonDateSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.util.Date;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Jackson2ObjectMapperBuilderCustomizer customizer = jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .serializerByType(BigInteger.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance)
                .serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Date.class, new JacksonDateSerializer())
//                        .serializerByType(LocalDate.class, new LocalDateSerializer())
//                        .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer())
                .deserializerByType(String.class, new StringDeserializer())
//                        .deserializerByType(Date.class, new DateDeserializer())
//                        .deserializerByType(LocalDate.class, new LocalDateDeserializer())
//                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer())
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false);
        return customizer;
    }
}