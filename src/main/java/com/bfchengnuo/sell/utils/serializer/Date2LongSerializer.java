package com.bfchengnuo.sell.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * json 转换时，将 date 转换成 long
 * 时间戳默认为毫秒数，我们转换成秒
 * Created by 冰封承諾Andy on 2018/7/19.
 */
public class Date2LongSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 没法位运算...
        jsonGenerator.writeNumber(date.getTime() / 1000);
    }
}
