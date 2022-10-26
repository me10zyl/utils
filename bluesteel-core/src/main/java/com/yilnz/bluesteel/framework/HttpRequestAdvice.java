package com.yilnz.bluesteel.framework;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * http请求切面 - 记录日志
 *
 * @author zyl
 * @date 2022/05/31
 */

public class HttpRequestAdvice implements MethodInterceptor {

    public static final Logger logger = LoggerFactory.getLogger(HttpRequestAdvice.class);

/*    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String globalTraceId = (String) request.getAttribute(Constants.REQ_GLOBAL_TRACE_ID);
        Object[] args = point.getArgs();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i] + "，");
        }
        if(sb.length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        String url = request.getRequestURL().toString();
        Object proceed = null;
        Throwable exception = null;
        try {
            proceed = point.proceed();
        }catch (Throwable e){
            exception = e;
        }finally {
            logger.info("\n----------------请求内容["+globalTraceId+"]----------------\n" +
                    "["+globalTraceId+"]请求URL：" + url + "\n" +
                    "["+globalTraceId+"]请求用户:" + request.getHeader("Authorization") + "\n" +
                    "["+globalTraceId+"]控制器:" + point.getTarget().getClass().getName() + "#" + point.getSignature().getName() + "\n" +
                    "["+globalTraceId+"]参数:" + sb.toString() + "\n" +
                    "["+globalTraceId+"]" + (exception == null ? "返回:" + proceed : "异常:" + exception) + "\n" +
                    "================请求内容["+globalTraceId+"]================");
            if (exception != null) {
                throw exception;
            }
        }
        return proceed;
    }*/

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String globalTraceId = (String) request.getAttribute(Constants.REQ_GLOBAL_TRACE_ID);
        Object[] args = methodInvocation.getArguments();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i] + "，");
        }
        if(sb.length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        String url = request.getRequestURL().toString();
        Object proceed = null;
        Throwable exception = null;
        try {
            proceed = methodInvocation.proceed();
        }catch (Throwable e){
            exception = e;
        }finally {
            logger.info("\n----------------请求内容["+globalTraceId+"]----------------\n" +
                    "["+globalTraceId+"]请求URL：" + url + "\n" +
                    "["+globalTraceId+"]请求用户:" + request.getHeader("Authorization") + "\n" +
                    "["+globalTraceId+"]控制器:" + methodInvocation.getThis().getClass() + "#" + methodInvocation.getMethod().getName() + "\n" +
                    "["+globalTraceId+"]参数:" + sb.toString() + "\n" +
                    "["+globalTraceId+"]" + (exception == null ? "返回:" + proceed : "异常:" + exception) + "\n" +
                    "================请求内容["+globalTraceId+"]================");
            if (exception != null) {
                throw exception;
            }
        }
        return proceed;
    }
}
