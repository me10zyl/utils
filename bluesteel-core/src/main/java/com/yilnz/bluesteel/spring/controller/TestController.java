package com.yilnz.bluesteel.spring.controller;

import com.yilnz.bluesteel.reflection.ClassScanner;
import com.yilnz.bluesteel.spring.controller.entity.AAPI;
import com.yilnz.bluesteel.spring.controller.entity.AMethod;
import com.yilnz.bluesteel.spring.controller.entity.AParam;
import com.yilnz.bluesteel.spring.controller.entity.AType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String testApis(Model model) throws IOException, ClassNotFoundException {
        final List<Class> classes = ClassScanner.scanPackcage("com.taojin.marking.web.api");
        final List<AAPI> apiNames = classes.parallelStream().map(e -> {
            final AAPI aapi = new AAPI();
            aapi.setName(e.getSimpleName());
            aapi.setFullClassName(e.getName());
            return aapi;
        }).collect(Collectors.toList());
        model.addAttribute("apisNames", apiNames);
        return "test/index";
    }

    @RequestMapping("/test/{apiName}")
    public String apiDetail(@PathVariable String apiName, @RequestParam String fullClassName, Model model) {
        try {
            final List<AMethod> methods = getaMethods(fullClassName);
            model.addAttribute("name", apiName);
            model.addAttribute("methods", methods);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "test/apiDetail";
    }

    private List<AMethod> getaMethods(@RequestParam String fullClassName) throws ClassNotFoundException {
        final Class<?> aClass = Class.forName(fullClassName);
        final Method[] declaredMethods = aClass.getDeclaredMethods();
        return Stream.of(declaredMethods).map(e -> {
            AMethod m = new AMethod();
            m.setName(e.getName());
            m.setClazz(fullClassName);
            final AType aType = new AType();
            aType.setName(e.getReturnType().getName());
            List<AParam> params = new ArrayList<>();
            final Parameter[] parameters = e.getParameters();
            final List<String> methodParameterNames = ClassScanner.getMethodParameterNames(aClass, e);
            for (int i = 0;i < parameters.length;i++) {
                Parameter parameter = parameters[i];
                final AParam param = new AParam();
                final AType paramType = new AType();
                paramType.setClazz(parameter.getType());
                paramType.setName(paramType.getClazz().getSimpleName());
                param.setType(paramType);
                param.setName(methodParameterNames.get(i));
                params.add(param);
            }
            m.setParams(params);
            m.setReturnType(aType);
            return m;
        }).collect(Collectors.toList());
    }

    @RequestMapping("/test/method")
    public String methodDetail(@RequestParam String clazz, @RequestParam String method, Model model) {
        try {
            final List<AMethod> methods = getaMethods(clazz);
            final List<AMethod> currentMethods = methods.parallelStream().filter(e -> e.getName().equals(method)).collect(Collectors.toList());
            AMethod m = currentMethods.get(0);
            model.addAttribute("method", m);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "test/apiMethod";
    }
}
