package com.yilnz.bluesteel.springbootplus.config;


import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.EnumSet;


/**
 * 杰克逊enum deserializer2
 *
 * @author zyl
 * @date 2021/06/16
 * @return
 */
@Slf4j
public class JacksonEnumDeserializer extends JsonDeserializer<Enum> {

    @Override
    public Enum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        Object currentValue = jsonParser.getCurrentValue();
        String currentName = jsonParser.getCurrentName();
        try {
            Field field = currentValue.getClass().getDeclaredField(currentName);
            Class<?> targetType = field.getType();

            Enum<?> target = EnumUtil.fromStringQuietly((Class<Enum>) targetType, text);
            if (target != null) {
                return target;
            }

            if(IEnum.class.isAssignableFrom(targetType)){
                EnumSet enumSet = EnumSet.allOf((Class<Enum>) targetType);
                for (Object o : enumSet) {
                    if(String.valueOf(((IEnum)o).getValue()).equals(text)){
                        target  = (Enum<?>) o;
                        break;
                    }
                }
                if(target == null){
                    target = Enum.valueOf((Class<Enum>)targetType, text);
                }
                if(target == null){
                    throw new RuntimeException("枚举转换错误！" + text + ":" + targetType);
                }
                return target;
            }
        } catch (NoSuchFieldException fieldException) {
           log.error("field ERROR", fieldException);
        }

        throw new RuntimeException("枚举转换错误！");
    }
}
