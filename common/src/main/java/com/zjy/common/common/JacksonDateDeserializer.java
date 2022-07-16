package com.zjy.common.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.io.IOException;
import java.util.Date;

public class JacksonDateDeserializer extends DateDeserializers.DateDeserializer {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return MyCustomDateEditor.parse(p.getText().trim());
    }
}
