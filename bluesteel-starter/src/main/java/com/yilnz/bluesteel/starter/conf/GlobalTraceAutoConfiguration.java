package com.yilnz.bluesteel.starter.conf;

import com.yilnz.bluesteel.framework.aop.HttpRequestAdvisor;
import com.yilnz.bluesteel.framework.globaltrace.GlobalTraceFilter;
import com.yilnz.bluesteel.starter.properties.GlobalTraceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GlobalTraceProperties.class)
@RequiredArgsConstructor
public class GlobalTraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "globalTraceFilter")
    @ConditionalOnProperty(value = "globaltrace.trace-id.enable", havingValue = "true")
    public FilterRegistrationBean globalTraceFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new GlobalTraceFilter(), new ServletRegistrationBean[0]);
        filterRegistrationBean.setOrder(Integer.MIN_VALUE);
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "globaltrace.http-request-aspect.enable")
    public HttpRequestAdvisor httpRequestAdvisor(GlobalTraceProperties properties){
        return new HttpRequestAdvisor(properties.getHttpRequestAspect().getAopPattern());
    }


}
