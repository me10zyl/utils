package com.yilnz.bluesteel.mockito;

import cn.hutool.core.util.RandomUtil;
import com.yilnz.bluesteel.reflection.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MockUtil {

    public static String randomString(){
        return RandomUtil.randomString(10);
    }

    public static int randomInt(){
        return RandomUtil.randomInt();
    }

    public static BigDecimal randomBigDecimal() {
        return RandomUtil.randomBigDecimal();
    }

    public static  <T> T mock(Class<T> type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return mock(type, "", "");
    }

    public static  <T> T mock(Class<T> type, String prefix, String suffix) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try {
            T t = type.getConstructor().newInstance();
            List<Method> setterMethods = ReflectionUtil.getAllSetterMethods(type);
            for (Method setterMethod : setterMethods) {
                Class<?>[] parameterTypes = setterMethod.getParameterTypes();
                if(parameterTypes[0].equals(String.class)){
                    setterMethod.invoke(t, prefix + randomString() + suffix);
                }else if(parameterTypes[0].equals(Date.class)){
                    setterMethod.invoke(t, new Date());
                }else if(parameterTypes[0].equals(Integer.class)){
                    setterMethod.invoke(t, randomInt());
                }else if(parameterTypes[0].equals(BigDecimal.class)){
                    setterMethod.invoke(t, randomBigDecimal());
                }
            }
            return t;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            //e.printStackTrace();
            throw e;
        }
    }


}
