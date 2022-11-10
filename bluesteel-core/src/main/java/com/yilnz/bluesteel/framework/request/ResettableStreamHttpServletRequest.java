package com.yilnz.bluesteel.framework.request;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {

        private byte[] rawData = {};
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public String requestId;
        public String payloadFilePrefix;
        public String payloadTarget;

        ResettableStreamHttpServletRequest(HttpServletRequest request) throws IOException {
                super(request);
                this.request = request;
                this.servletStream = new ResettableServletInputStream();
        }

        void resetInputStream() throws IOException {
                initRawData();
                servletStream.inputStream = new ByteArrayInputStream(rawData);
        }

        private void initRawData() throws IOException {
                if ( rawData.length == 0 ) {
                        byte[] b = IOUtils.toByteArray(this.request.getInputStream());
                        if ( b != null )
                                rawData = b;
                }
                servletStream.inputStream = new ByteArrayInputStream(rawData);
        }
        @Override
        public ServletInputStream getInputStream() throws IOException {
                initRawData();
                return servletStream;
        }
        public BufferedReader getReader() throws IOException {
                initRawData();
                String encoding = getCharacterEncoding();
                if ( encoding != null ) {
                        return new BufferedReader(new InputStreamReader(servletStream, encoding));
                } else {
                        return new BufferedReader(new InputStreamReader(servletStream));
                }
        }
}