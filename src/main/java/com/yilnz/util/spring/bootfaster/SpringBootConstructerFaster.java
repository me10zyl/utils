package com.yilnz.util.spring.bootfaster;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SpringBootConstructerFaster implements BeanFactoryPostProcessor, Ordered {

    public static Map<Object, Instant> launchTime = new HashMap<>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            launchTime.put(beanDefinitionName, Instant.now());
        }
    }

    public Instant getLauchTime(String beanName){
        return launchTime.get(beanName);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
