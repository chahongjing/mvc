package com.zjy.service.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zjy.baseframework.interfaces.IBaseEnum;
import com.zjy.service.common.DateFormaterFilter;
import com.zjy.common.utils.EnumUtils;
import com.zjy.common.utils.EnumSerializer;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FastJsonConfiguration {
    @Bean
    public HttpMessageConverters customConverters() {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        supportedMediaTypes.add(MediaType.TEXT_HTML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig());
        //支持text 转string
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        return new HttpMessageConverters(fastConverter, stringHttpMessageConverter);
    }

    @Bean
    public FastJsonConfig fastJsonConfig() {
        // 创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        // region 此处是全局处理方式
//        fastJsonConfig.setFeatures(Feature.SupportAutoType);
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.SkipTransientField
//                SerializerFeature.WriteClassName
        );
        fastJsonConfig.setSerializeFilters(new DateFormaterFilter());
        // endregion

        // region 序列化
        SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();
        fastJsonConfig.setSerializeConfig(serializeConfig);

        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
//        serializeConfig.put(LocalDateTime.class, (serializer, object, fieldName, fieldType, features) -> {
//            SerializeWriter out = serializer.out;
//            if (object == null) {
//                out.writeNull();
//                return;
//            }
//            out.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format((LocalDateTime) object));
//        });
//        serializeConfig.put(LocalDate.class, (serializer, object, fieldName, fieldType, features) -> {
//            SerializeWriter out = serializer.out;
//            if (object == null) {
//                out.writeNull();
//                return;
//            }
//            out.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate) object));
//        });
//        serializeConfig.put(LocalTime.class, (serializer, object, fieldName, fieldType, features) -> {
//            SerializeWriter out = serializer.out;
//            if (object == null) {
//                out.writeNull();
//                return;
//            }
//            out.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format((LocalTime) object));
//        });
        // endregion

        // region 反序列化
        ParserConfig parserConfig = fastJsonConfig.getParserConfig();
        // endregion

        // 枚举序列化和反序列化
        for (Class<IBaseEnum> enumClass : EnumUtils.getEnumList()) {
            serializeConfig.put(enumClass, EnumSerializer.instance);
            parserConfig.putDeserializer(enumClass, EnumSerializer.instance);
        }

        return fastJsonConfig;
    }
}
