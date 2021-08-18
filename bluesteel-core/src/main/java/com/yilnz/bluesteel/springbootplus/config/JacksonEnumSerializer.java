package com.yilnz.bluesteel.springbootplus.config;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.Serializable;

/**
 * enum序列化器   simpleModule.addSerializer(IEnum.class, new JacksonEnumSerializer());
 *         simpleModule.setDeserializerModifier(new JacksonEnumModifier());
 *
 * @author zyl
 * @date 2021/06/08
 * @return
 */
public class JacksonEnumSerializer extends JsonSerializer<IEnum> {


    @Override
    public void serialize(IEnum iEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Serializable value = null;
        if (iEnum != null) {
             value = iEnum.getValue();
        }
        if(value instanceof Number) {
            jsonGenerator.writeNumber((Integer) value);
        }else{
            jsonGenerator.writeString((String) value);
        }
    }
}
