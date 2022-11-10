package com.yilnz.bluesteel.framework.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RequestFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);
    private List<String> whiteList = new ArrayList<>();


    public RequestFilter(LogApiInterceptor logApiInterceptor) {
        this.logApiInterceptor = logApiInterceptor;
    }

    public RequestFilter(LogApiInterceptor logApiInterceptor, List<String> whiteList) {
        this.logApiInterceptor = logApiInterceptor;
        this.whiteList = whiteList;
    }

    LogApiInterceptor logApiInterceptor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
        // LOG REQUEST
        ResettableStreamHttpServletRequest wrappedRequest = null;
        ResettableStreamHttpServletResponse wrappedResponse = null;
        try {
                if(whiteList.contains(((HttpServletRequest)request).getRequestURI())){
                    return;
                }
                wrappedRequest = new ResettableStreamHttpServletRequest((HttpServletRequest) request);
                wrappedResponse = new ResettableStreamHttpServletResponse((HttpServletRequest)request, (HttpServletResponse) response);
                logApiInterceptor.writeRequestPayloadAudit(wrappedRequest);
        } catch (Exception e) {
                LOGGER.error("Fail to wrap request and response",e);
        }
        chain.doFilter(wrappedRequest, wrappedResponse);
    }
}
