package com.fans.common.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * JSON 文本字段反序列化：
 * 数据库里存 JSON 字符串，输出时还原为数组/对象。
 */
public class JsonTextSer extends JsonSerializer<String> {

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isBlank()) {
            gen.writeNull();
            return;
        }
        String text = value.trim();
        try {
            gen.writeTree(OM.readTree(text));
        } catch (Exception e) {
            gen.writeString(value);
        }
    }
}
