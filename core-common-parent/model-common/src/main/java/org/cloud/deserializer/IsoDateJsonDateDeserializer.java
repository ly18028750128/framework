package org.cloud.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.TimeZone;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IsoDateJsonDateDeserializer extends JsonDeserializer<Date> {

    public final static SimpleDateFormat defaultDateFormat = DateTimeFormat.ISODATE.getDateFormat();
    static {
        defaultDateFormat.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        if (jp == null || StringUtils.isEmpty(jp.getText())) {
            return null;
        }

        String date = jp.getText();
        try {
            return defaultDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}