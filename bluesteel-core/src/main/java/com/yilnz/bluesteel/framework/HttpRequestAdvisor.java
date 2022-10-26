package com.yilnz.bluesteel.framework;

import lombok.AllArgsConstructor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class HttpRequestAdvisor {

    public String aopExecution;

    public HttpRequestAdvisor(String aopExecution) {
        this.aopExecution  = aopExecution;
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(){
        HttpRequestAdvice advice = new HttpRequestAdvice();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(aopExecution);

        // 配置增强类advisor
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(advice);
        return advisor;
    }
}
