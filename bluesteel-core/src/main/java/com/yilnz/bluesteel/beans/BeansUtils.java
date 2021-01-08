package com.yilnz.bluesteel.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BeansUtils {

    public static <T> T copyProperties(Object source, Class<T> targetClass){
        T o = null;
        try {
            o = (T) targetClass.getConstructors()[0].newInstance();
            BeanUtils.copyProperties(source, o);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("beansUtils err", e);
        }
        return o;
    }

    public static <T> List<T> copyPropertiesToList(List<?> source, Class<T> targetClass){
        List<T> list = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            try {
                Object o = targetClass.getConstructors()[0].newInstance();
                BeanUtils.copyProperties(source.get(i), o);
                list.add((T) o);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.error("beansUtils err", e);
            }
        }
        return list;
    }
}
