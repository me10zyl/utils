package com.yilnz.bluesteel.framework.globaltrace;

import com.yilnz.bluesteel.framework.Constants;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class GlobalTraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        MDC.put(Constants.MDC_REQUEST_URI, ((HttpServletRequest)request).getRequestURI());
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String header = ((HttpServletRequest)request).getHeader(Constants.X_REQUEST_ID);
        if(header != null){
            traceId = header;
        }
        MDC.put(Constants.MDC_GLOBAL_TRACE_ID, traceId);
        request.setAttribute(Constants.REQ_GLOBAL_TRACE_ID, traceId);
        filterChain.doFilter(request, response);
        Object attribute = request.getAttribute(Constants.REQ_GLOBAL_TRACE_ID);
        if(attribute != null) {
            ((HttpServletResponse)response).setHeader(Constants.X_REQUEST_ID, attribute.toString());
        }
    }
}
