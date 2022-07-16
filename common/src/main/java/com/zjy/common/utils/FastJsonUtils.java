package com.zjy.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.zjy.baseframework.interfaces.IBaseEnum;
import com.zjy.common.common.DateFormaterFilter;
import com.zjy.common.common.EnumSerializer;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FastJsonUtils implements JsonUtils {
    FastJsonConfig fastJsonConfig;

    private FastJsonUtils() {
        fastJsonConfig = getFastJsonConfig();
    }

    public <T> T parse(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz, fastJsonConfig.getParserConfig());
    }

    public <T> List<T> parseList(String json, Class<T> tClass) {
        return JSON.parseArray(json, tClass, fastJsonConfig.getParserConfig());
    }

    public <T> T[] parseArray(String json, Class<T> tClass) {
        List<T> list = parseList(json, tClass);
        if (list == null) return null;
        T[] result = (T[]) Array.newInstance(tClass, list.size());
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public String toJSON(Object obj) {
        return JSON.toJSONString(obj, fastJsonConfig.getSerializeConfig());
    }

    public static FastJsonConfig getFastJsonConfig() {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
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
