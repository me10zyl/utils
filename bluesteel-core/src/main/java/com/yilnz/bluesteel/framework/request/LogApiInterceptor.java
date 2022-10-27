package com.yilnz.bluesteel.framework.request;

import com.yilnz.bluesteel.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;


public class LogApiInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogApiInterceptor.class);
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            if ( response instanceof ResettableStreamHttpServletResponse ) {
                    ((ResettableStreamHttpServletResponse)response).payloadFilePrefix = ((ResettableStreamHttpServletRequest)request).payloadFilePrefix;
                    ((ResettableStreamHttpServletResponse)response).payloadTarget  = ((ResettableStreamHttpServletRequest)request).payloadTarget;
                    writeResponsePayloadAudit(request, (ResettableStreamHttpServletResponse) response);
            }
    }
    public String getRawHeaders(HttpServletRequest request) {
            String globalTraceId = (String) request.getAttribute(Constants.REQ_GLOBAL_TRACE_ID);
            StringBuffer rawHeaders = new StringBuffer();
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                    String key = (String) headerNames.nextElement();
                    String value = request.getHeader(key);
                    rawHeaders.append(key).append(":").append(value).append("\n");
            }

            return rawHeaders.toString();
    }
    public String getRawHeaders(HttpServletRequest request, HttpServletResponse response){
            String globalTraceId = (String) request.getAttribute(Constants.REQ_GLOBAL_TRACE_ID);
            StringBuffer rawHeaders = new StringBuffer();
            Enumeration headerNames = Collections.enumeration(response.getHeaderNames());
            while (headerNames.hasMoreElements()) {
                    String key = (String) headerNames.nextElement();
                    String value = response.getHeader(key);
                    rawHeaders.append(key).append(":").append(value).append("\n");
            }

            return rawHeaders.toString();
    }
    private void writePayloadAudit(String payloadFile, String rawHeaders, String requestBody) throws IOException {
            try (Writer writer = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(payloadFile), StandardCharsets.UTF_8))) {
                    writer.write(rawHeaders);
                    writer.write("\n");
                    writer.write(requestBody);
            }
    }
    public void writeRequestPayloadAudit(ResettableStreamHttpServletRequest wrappedRequest) {
	    try {
		    String requestHeaders = getRawHeaders(wrappedRequest);
		    String requestBody = org.apache.commons.io.IOUtils.toString(wrappedRequest.getReader());
            LOGGER.info("请求路径===========>" + wrappedRequest.getRequestURL().toString());
		    LOGGER.info("请求方法===========>"+ wrappedRequest.getMethod());
		    LOGGER.info("请求头============>");
            LOGGER.info(requestHeaders);
		    LOGGER.info("请求体============>");
		    LOGGER.info(requestBody);
	    } catch (Exception e) {
		    LOGGER.error("ERRR", e);
	    }
    }
    public void writeResponsePayloadAudit(HttpServletRequest req, ResettableStreamHttpServletResponse wrappedResponse){
        String rawHeaders = getRawHeaders(req, wrappedResponse);
	    LOGGER.info("<============响应码 " + wrappedResponse.getStatus());
	    LOGGER.info("<============响应头");
        LOGGER.info(rawHeaders);
	    LOGGER.info("<============响应体");
	    byte[] data = new byte[wrappedResponse.rawData.size()];
	    for (int i = 0; i < data.length; i++) {
		data[i] = (byte) wrappedResponse.rawData.get(i);
	    }
	    String responseBody = new String(data);
	    LOGGER.info(responseBody);
    }
}
