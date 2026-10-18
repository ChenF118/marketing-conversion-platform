package com.fans.common.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/** 0 -> false, 1 -> true（布尔字段序列化） */
public class IntBoolSer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else if (value == 0) {
            gen.writeBoolean(false);
        } else if (value == 1) {
            gen.writeBoolean(true);
        } else {
            gen.writeNull();
        }
    }
}
