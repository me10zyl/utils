package com.yilnz.bluesteel.springbootplus.utils;

import cn.hutool.core.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * enum跑龙套
 *
 * @author zyl
 * @date 2021/08/18
 */
@Slf4j
public class EnumUtils {

   /* @Getter
        @AllArgsConstructor
        public enum MaterialsClass implements IEnum<Integer> {
            MUST_LEARN("必学教程",1),EXAMPLE_LEARN("案例学习", 2);

            private String name;
            private Integer value;

            @Override
            public Integer getValue() {
                return value;
            }
        }
    */


    /**
     * 序列化枚举
     *
     * @param iEnumClass 我枚举类
     * @return {@link List<Map<String, Object>> }
     * @author zyl
     * @date 2021/06/10
     */
    public static List<Map<String, Object>> serializeEnum(Class<? extends Enum> iEnumClass){
        try {
            LinkedHashMap enumMap = EnumUtil.getEnumMap(iEnumClass);
            List<Map<String,Object>> list = new ArrayList<>();
            for (Object o : enumMap.entrySet()) {
                Map<String, Object> map = new HashMap<>();
                Enum e = (Enum) ((Map.Entry)o).getValue();
                map.put("fieldName", e.name());
                map.put("value", iEnumClass.getDeclaredMethod("getValue").invoke(e));
                map.put("name", iEnumClass.getDeclaredMethod("getName").invoke(e));
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            log.error("enumUtils err", e);
        } finally {
        }
        return null;
    }

}
