package org.cloud.deserializer;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cloud.model.TFrameRole;

import java.io.IOException;
import java.util.Collection;

public class GrantedFrameRoleDeserializer extends JsonDeserializer<Collection<? extends TFrameRole>> {
    @Override
    public Collection<? extends TFrameRole> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            return JSON.parseArray("[]", TFrameRole.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
