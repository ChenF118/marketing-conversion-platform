package com.fans.common.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/** Cvslink::getcvsmodeAttr: 0=模糊匹配, 1=精确匹配 */
public class CvsmodeSer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeString("");
        } else if (value == 0) {
            gen.writeString("模糊匹配");
        } else if (value == 1) {
            gen.writeString("精确匹配");
        } else {
            gen.writeNull();
        }
    }
}
