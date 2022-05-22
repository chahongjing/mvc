package com.zjy.service.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.zjy.common.MyCustomDateEditor;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JacksonDateSerializer extends DateSerializer {
    @Override
    public void serialize(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (value == null) {
            this._serializeAsString(value, g, provider);
            return;
        }
        g.writeString(DateFormatUtils.formatUTC(value, ((SimpleDateFormat) MyCustomDateEditor.getUtcSfd()).toPattern()));
    }
}
