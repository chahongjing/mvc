package com.zjy.web.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.zjy.baseframework.interfaces.IBaseEnum;

import java.io.IOException;

public class EnumSerializer extends JsonSerializer<IBaseEnum> {
    @Override
    public void serialize(IBaseEnum e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(e.getValue());
    }
}
