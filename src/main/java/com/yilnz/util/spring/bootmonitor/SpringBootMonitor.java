package com.yilnz.util.spring.bootmonitor;

import com.yilnz.util.spring.bootfaster.SpringBootConstructerFaster;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SpringBootMonitor implements BeanPostProcessor, Ordered {

    public static Map<Object, Instant> startTimeMap = new HashMap<>();
    public static Map<Object, Instant> endTimeMap = new HashMap<>();
    public static Map<Object, Duration> durationMap = new HashMap<>();

    @Autowired
    private SpringBootConstructerFaster constructerMonitor;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        startTimeMap.put(beanName, Instant.now());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        endTimeMap.put(beanName, Instant.now());
        return bean;
    }

    public long getInitializationTime(String beanName) {
        Instant startTime = startTimeMap.get(beanName);
        Instant endTime = endTimeMap.get(beanName);
        final Instant lauchTime = constructerMonitor.getLauchTime(beanName);
        if (startTime != null) {
            endTime = endTime.compareTo(lauchTime) > 0 ? endTime : lauchTime;
            durationMap.putIfAbsent(beanName, Duration.between(startTime, endTime));
        }
        final Duration duration = durationMap.getOrDefault(beanName, Duration.ZERO);
        return duration.toMillis();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
