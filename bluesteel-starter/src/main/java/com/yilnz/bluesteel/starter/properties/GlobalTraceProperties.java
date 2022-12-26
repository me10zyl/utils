package com.yilnz.bluesteel.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "globaltrace")
@Data
public class GlobalTraceProperties {
    private TraceId traceId;
    private HttpRequestAspect httpRequestAspect;
    private HttpLog httpLog;

    @Data
    public static class TraceId {
        private boolean enable;
    }
    @Data
    public static class HttpRequestAspect {
        private boolean enable;
        private String aopPattern;
    }
    @Data
    public static class HttpLog {
        private boolean enable;
    }
}
