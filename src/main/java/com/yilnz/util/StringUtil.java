package com.yilnz.util;

import com.google.common.base.CaseFormat;
import com.yilnz.util.reflection.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String exceptionToString(Exception exception){
		final StackTraceElement[] stackTrace = exception.getStackTrace();
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement stackTraceElement : stackTrace) {
			sb.append(stackTraceElement.toString()).append(System.lineSeparator());
		}
		return sb.toString();
	}

	public static String format(String message, Object... arguments){
		final Matcher matcher = Pattern.compile("\\{\\}").matcher(message);
		int i = 0;
		String handleMsg = message;
		while (matcher.find()){
			handleMsg = matcher.replaceFirst("{" + i + "}");
			matcher.reset(handleMsg);
			i++;
		}
		return MessageFormat.format(handleMsg, arguments);
	}

    public static boolean isBlank(String str){
        return StringUtils.isBlank(str);
    }

    public static boolean isAnyBlank(String... str){
        boolean blank = false;
        for (String s : str) {
            if(isBlank(s)){
                blank = true;
                break;
            }
        }
        return blank;
    }

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
