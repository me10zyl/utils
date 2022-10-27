package com.yilnz.bluesteel.framework.aop;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;

public class HttpRequestAdvisor extends DefaultPointcutAdvisor {
    public String aopExecution;

    public HttpRequestAdvisor(String aopExecution) {
        this.aopExecution  = aopExecution;
        HttpRequestAdvice advice = new HttpRequestAdvice();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(aopExecution);
        setPointcut(pointcut);
        setAdvice(advice);
    }

//    private DefaultPointcutAdvisor defaultPointcutAdvisor(){
//        HttpRequestAdvice advice = new HttpRequestAdvice();
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression(aopExecution);
//        // 配置增强类advisor
//        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
//        advisor.setPointcut(pointcut);
//        advisor.setAdvice(advice);
//        return advisor;
//    }
}
