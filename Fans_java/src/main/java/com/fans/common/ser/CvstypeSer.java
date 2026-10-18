package com.fans.common.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/** Cvslink::getCvstypeAttr: 1=点击, 2=长按, 其它为空串 */
public class CvstypeSer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeString("");
        } else if (value == 1) {
            gen.writeString("点击");
        } else if (value == 2) {
            gen.writeString("长按");
        } else {
            gen.writeNull();
        }
    }
}
