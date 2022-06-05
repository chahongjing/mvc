package com.zjy.service.common;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.zjy.baseframework.interfaces.IBaseEnum;

import java.io.IOException;
import java.lang.reflect.Type;

public class EnumSerializer extends JsonSerializer<IBaseEnum> implements ObjectSerializer, ObjectDeserializer {
    public static final EnumSerializer instance = new EnumSerializer();
    @Override
    public void serialize(IBaseEnum e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Integer value = getValue(e);
        if(value == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeNumber(value);
        }
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        Integer value = getValue((IBaseEnum)object);
        if(value == null) {
            out.writeNull();
        } else {
            out.writeInt(value);
        }
    }

    private Integer getValue(IBaseEnum value) {
        if(value == null) return null;
        return value.getValue();
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        Integer intValue = parser.parseObject(int.class);
        if(intValue != null) {
            for (T enumConstant : ((Class<T>) type).getEnumConstants()) {
                if(intValue.equals(((IBaseEnum)enumConstant).getValue())) {
                    return enumConstant;
                }
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
