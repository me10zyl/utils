package com.yilnz.bluesteel.reflection;

import org.reflections.Reflections;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * stackoverflow anwser https://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
 */
public class ClassScanner  {
    private final static Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        final List<Class> classes = scanPackcage("com.yilnz.util");
        System.out.println(classes);
        final List<String> parameterNames = ClassScanner.getMethodParameterNames(ClassScanner.class, "getMethodParameterNames");
        System.out.println(parameterNames);
    }

    public static List<Class> scanPackage(ApplicationContext context, String packageName) {
        System.out.println(context.getBeanDefinitionNames());
        return new ArrayList<>();
    }


    public static List<String> getMethodParameterNames(Class clazz, String methodName) {
        final Method[] declaredMethods = clazz.getMethods();
        Method mName = null;
        for (int i = 0; i < declaredMethods.length; i++) {
            if (declaredMethods[i].getName().equals(methodName)) {
                mName = declaredMethods[i];
            }
        }
        return getMethodParameterNames(clazz, mName);
    }

    public static List<String> getMethodParameterNames(Class clazz, Method method) {
        List<String> methodParamNames = new ArrayList<>();
        try {
            if (method == null) {
                return null;
            }
            if (clazz.isInterface() && applicationContext != null) {
                clazz = applicationContext.getBean(clazz).getClass();
                method = clazz.getMethod(method.getName(), method.getParameterTypes());
            }
            final Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                if (!parameter.isNamePresent()) {
                    break;
                }
                methodParamNames.add(parameter.getName());
            }
            if (methodParamNames.size() == 0) {
                final Reflections reflections = getReflectionsByClassName(clazz);
                if (reflections != null) {
                    methodParamNames = reflections.getMethodParamNames(method);
                }
            }
            if (methodParamNames.size() == 0) {
                methodParamNames = defaultDiscover(method);

            }
            if (methodParamNames.size() == 0 && applicationContext != null) {
                clazz = clazz.getSuperclass();
                if(clazz == null){
                    return methodParamNames;
                }
                return getMethodParameterNames(clazz, clazz.getMethod(method.getName(), method.getParameterTypes()));

            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return methodParamNames;
    }

    private static List<String> defaultDiscover(Method method) {
        List<String> methodParamNames = new ArrayList<>();
        final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        final String[] parameterNames = discoverer.getParameterNames(method);
        if (!(parameterNames == null || parameterNames.length == 0)) {
            methodParamNames = Arrays.asList(parameterNames);
        }
        return methodParamNames;
    }


    public static List<Class> scanPackcage(String packageName) {
        Reflections reflections = getReflectionsByPackage(packageName);
        final Set<String> allTypes = reflections.getAllTypes();
        List<Class> allTypesList = new ArrayList<>();
        try {
            for (String allType : allTypes) {
                final Class<?> aClass = Class.forName(allType);
                allTypesList.add(aClass);
            }
        } catch (ClassNotFoundException e1) {
            logger.error("[YILNZ-UTIL]ClassScanner ERROR", e1);
        }
        return allTypesList;
    }

    private static Reflections getReflectionsByClassName(Class className) {
        Reflections reflections = null;
        try {
             reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forClass(className))
                    .setScanners(new MethodParameterNamesScanner())
                    .filterInputsBy(new FilterBuilder().includePackage(className))
            );
        }catch (NullPointerException nep){

        }
        return reflections;
    }

    private static Reflections getReflectionsByPackage(String packageName) {
        return new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(false))
                .filterInputsBy(new FilterBuilder().includePackage(packageName))
        );
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
