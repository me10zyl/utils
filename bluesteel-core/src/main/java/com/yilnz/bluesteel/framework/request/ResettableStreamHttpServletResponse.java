package com.yilnz.bluesteel.framework.request;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResettableStreamHttpServletResponse extends HttpServletResponseWrapper {

        public String requestId;
        public String payloadFilePrefix;
        public String payloadTarget;

        public List<Byte> rawData = new ArrayList<Byte>();
        public HttpServletResponse response;
        private ResettableServletOutputStream servletStream;
        private HttpServletRequest request;

        ResettableStreamHttpServletResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
                super(response);
                this.request = request;
                this.response = response;
                this.servletStream = new ResettableServletOutputStream( request,this);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
                return servletStream;
        }
        public PrintWriter getWriter() throws IOException {
                String encoding = getCharacterEncoding();
                if ( encoding != null ) {
                        return new PrintWriter(new OutputStreamWriter(servletStream, encoding));
                } else {
                        return new PrintWriter(new OutputStreamWriter(servletStream));
                }
        }
}
