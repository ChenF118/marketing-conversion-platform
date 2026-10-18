package com.fans.common.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/** Monitor::getTypeAttr: 1..17 的行为类型文案 */
public class MonitorTypeSer extends JsonSerializer<Integer> {
    private static final String[] TEXT = {
            "", "点击", "长按识别", "微信复制按钮点击", "微信打开按钮点击", "咨询按钮点击",
            "下载按钮点击", "购买按钮点击", "注册按钮点击", "登录按钮点击", "拨打电话按钮点击",
            "短信按钮点击", "加入购物车按钮点击", "地图按钮点击", "QQ咨询按钮点击", "抽奖按钮点击",
            "投票按钮点击", "表单确认提交按钮点击"
    };

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else if (value >= 1 && value < TEXT.length) {
            gen.writeString(TEXT[value]);
        } else {
            gen.writeNull();
        }
    }
}
