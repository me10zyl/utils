package com.yilnz.util.reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {
    public static boolean isChildOfType(Class childClass, Class superClass){
        final List<Class<?>> superClasses = getSuperClasses(childClass);
        return superClasses.stream().anyMatch(e -> e.equals(superClass));
    }

    public static List<Class<?>> getSuperClasses(Class clazz){
        List<Class<?>> classList = new ArrayList<>();
        Class tmp = clazz.getSuperclass();
        do{
            classList.add(tmp);
        }while ((tmp = tmp.getSuperclass()) != null);
        return classList;
    }
}
