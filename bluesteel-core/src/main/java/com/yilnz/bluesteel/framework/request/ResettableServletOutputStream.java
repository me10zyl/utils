package com.yilnz.bluesteel.framework.request;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

public class ResettableServletOutputStream extends ServletOutputStream {

	private static Logger logger = LoggerFactory.getLogger(ResettableServletOutputStream.class);

        @Autowired
        LogApiInterceptor logApiInterceptor;

        public OutputStream outputStream;

        private HttpServletRequest request;
        private ResettableStreamHttpServletResponse wrappedResponse;
        private ServletOutputStream servletOutputStream = new ServletOutputStream(){
                boolean isFinished = false;
                boolean isReady = true;
                WriteListener writeListener = null;

                @Override
                public void setWriteListener(WriteListener writeListener) {
                        this.writeListener = writeListener;
                }

                public boolean isReady(){
                        return isReady;
                }
                @Override
                public void write(int w) throws IOException{
                        outputStream.write(w);
                        wrappedResponse.rawData.add(new Integer(w).byteValue());
                }
        };

        public ResettableServletOutputStream(HttpServletRequest request, ResettableStreamHttpServletResponse wrappedResponse) throws IOException {
                this.outputStream = wrappedResponse.response.getOutputStream();
                this.request = request;
                this.wrappedResponse = wrappedResponse;
        }

        @Override
        public void close() throws IOException {
                System.out.println("** RESPONSE CLOSE **");
                outputStream.close();
                logApiInterceptor.writeResponsePayloadAudit(request, wrappedResponse);
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
                servletOutputStream.setWriteListener( writeListener );
        }
        @Override
        public boolean isReady(){
                return servletOutputStream.isReady();
        }

        @Override
        public void write(int w) throws IOException {
                servletOutputStream.write(w);
        }
}
