package com.yilnz.bluesteel.springbootplus.config;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

/**
 * 杰克逊enum修饰符, JACKSonConfig #102    simpleModule.addSerializer(IEnum.class, new JacksonEnumSerializer());
 *         simpleModule.setDeserializerModifier(new JacksonEnumModifier());
 *
 * @author zyl
 * @date 2021/06/16
 * @return
 */
public class JacksonEnumModifier extends BeanDeserializerModifier {


    @Override
    public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        if(IEnum.class.isAssignableFrom(beanDesc.getBeanClass())){
            return new JacksonEnumDeserializer();
        }
        return deserializer;
    }
}
