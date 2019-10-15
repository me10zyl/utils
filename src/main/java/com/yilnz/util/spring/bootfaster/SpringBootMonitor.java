package com.yilnz.util.spring.bootfaster;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SpringBootMonitor implements BeanPostProcessor {

    public static Map<Object, Instant> startTimeMap = new HashMap<>();
    public static Map<Object, Duration> durationMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final Instant now = Instant.now();
        startTimeMap.put(bean, now);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final Instant startInclusive = startTimeMap.get(bean);
        if (startInclusive != null) {
            durationMap.put(bean, Duration.between(startInclusive, Instant.now()));
        }
        System.out.println(durationMap);
        return bean;
    }
}
