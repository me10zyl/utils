package com.yilnz.util;

import com.google.common.base.CaseFormat;
import com.yilnz.util.reflection.ClassScanner;
import com.yilnz.util.reflection.ReflectionUtil;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringUtil {

    public static String camelCaseToUnderScore(String fieldName){
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    public static String toLowerCaseLetter1(String str){
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toUpperCaseLetter1(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String classToJsonString(Class<?> clazz){
        final List<Method> setters = getSetters(clazz);
        StringBuilder sb = new StringBuilder("{");
        for (Method setter : setters) {
            final String name = toLowerCaseLetter1(setter.getName().substring(3));
            sb.append(name + ":" + getClassInitValue(setter.getParameterTypes()[0]) + ",\n");
        }
        final String substring = sb.substring(0, sb.length() - 2);
        sb = new StringBuilder(substring);
        sb.append("}");
        return sb.toString();
    }

    private static List<Method> getSetters(Class<?> clazz){
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> list = new ArrayList<>();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.getName().startsWith("set")){
                list.add(declaredMethod);
            }
        }
        return list;
    }

    private static Object getClassInitValue(Class<?>  clazz){
        if (ReflectionUtil.isChildOfType(clazz, Number.class)) {
            return 0;
        }else if(ReflectionUtil.isChildOfType(clazz, Date.class)){
            return DateUtil.now();
        } else {
            return "\"\"";
        }
    }
}
