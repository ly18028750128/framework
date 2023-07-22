package org.cloud.deserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.cloud.constant.CoreConstant;

import java.io.IOException;
import java.text.DateFormat;

public class IsoDateStringJsonSerializable extends DateTimeSerializerBase<String> {


    protected IsoDateStringJsonSerializable(Class<String> type, Boolean useTimestamp, DateFormat customFormat) {
        super(type, useTimestamp, CoreConstant.DateTimeFormat.ISODATE.getDateFormat());
    }

    @Override
    public DateTimeSerializerBase<String> withFormat(Boolean aBoolean, DateFormat dateFormat) {
        return null;
    }

    @Override
    protected long _timestamp(String s) {
        return 0;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

    }
}
