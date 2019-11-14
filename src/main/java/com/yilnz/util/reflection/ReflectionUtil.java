package com.yilnz.util.reflection;

import com.yilnz.util.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ReflectionUtil {

    public static boolean isChildOfType(Class childClass, Class superClass){
        final List<Class<?>> superClasses = getSuperClasses(childClass);
        return superClasses.stream().anyMatch(e -> e.equals(superClass));
    }

    public static List<Class<?>> getSuperClasses(Class clazz){
        List<Class<?>> classList = new ArrayList<>();
        Class tmp = clazz;
        while ((tmp = tmp.getSuperclass()) != null) {
            classList.add(tmp);
        }
        return classList;
    }

    public static List<String> getGetterFields(Class<?> clazz){
        return getGetterFields(clazz, false);
    }

    public static List<String> getGetterFields(Class<?> clazz, boolean includeSuper){
        List<Method> list = getGetterMethods(clazz,includeSuper);
        return getGetterFields(list);
    }

    public static List<String> getGetterFields(List<Method> methodList) {
        return methodList.stream().map(e -> StringUtil.toLowerCaseLetter1(e.getName().substring(3))).collect(Collectors.toList());
    }

    public static List<Method> getGetterMethods(Class<?> clazz) {
        return getGetterMethods(clazz, false);
    }

    public static List<Method> getGetterMethods(Class<?> clazz, boolean includeSuper) {
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> list = new ArrayList<>();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.getName().startsWith("get")){
                list.add(declaredMethod);
            }
        }
        if(includeSuper && clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)){
            final List<Method> getterMethods = getGetterMethods(clazz.getSuperclass(), true);
            list.addAll(getterMethods);
        }
        return list;
    }
}
