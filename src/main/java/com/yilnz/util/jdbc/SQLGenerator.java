package com.yilnz.util.jdbc;

import com.yilnz.util.base.StringUtil;
import com.yilnz.util.reflection.ReflectionUtil;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class SQLGenerator {

    private static String getMySQLMappingType(Class type){
        Map<Class, String> mapping = new HashMap<>();
        mapping.put(Number.class, "int");
        mapping.put(Long.class, "bigint");
        mapping.put(CharSequence.class, "varchar(50)");
        mapping.put(Date.class, "datetime");
        mapping.put(BigDecimal.class, "decimal(10, 4)");

        final String s1 = mapping.get(type);
        if (s1 != null) {
            return s1;
        }
        final Class superclass = type.getSuperclass();
        final Class[] interfaces = type.getInterfaces();
        List<Class> allClass = new ArrayList<>(Arrays.asList(interfaces));
        allClass.add(superclass);
        String ss = null;
        for (Class anInterface : allClass) {
            final String s = mapping.get(anInterface);
            if (s != null) {
                ss = s;
                break;
            }
        }
        if (ss == null) {
            return "int";
        }
        return ss;
    }

    public static String generateDDLFromClass(Class<?> clazz) {
        return generateDDLFromClass(clazz, true);
    }

    public static String generateDDLFromClass(Class<?> clazz, boolean notNull) {
        StringBuilder sb = new StringBuilder("create table " + StringUtil.camelCaseToUnderScore(clazz.getSimpleName()) + "\n(\n");
        final List<Method> getterFields = ReflectionUtil.getGetterMethods(clazz, true);
        for (int i = 0; i < getterFields.size(); i++) {
            Method getterField = getterFields.get(i);
            final String s = StringUtil.camelCaseToUnderScore(getterField.getName().substring(3));
            sb.append(s + " " + getMySQLMappingType(getterField.getReturnType()));
            if (s.equals("id")) {
                sb.append("auto_increment primary key");
            }else{
                if (notNull) {
                    sb.append(" not null");
                }
            }
            if(i != getterFields.size() - 1){
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(");");
        return sb.toString();
    }
}
