package com.fans.common.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/** Weixin::getControlAttr: 0关闭 1按复制次数 2按时间段 3按上报到粉数 4按二维码长按识别数 5按数据上报成功 */
public class ControlSer extends JsonSerializer<Integer> {
    private static final String[] TEXT = {
            "关闭", "按复制次数", "按时间段", "按上报到粉数", "按二维码长按识别数", "按数据上报成功"
    };

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else if (value >= 0 && value < TEXT.length) {
            gen.writeString(TEXT[value]);
        } else {
            gen.writeNull();
        }
    }
}
