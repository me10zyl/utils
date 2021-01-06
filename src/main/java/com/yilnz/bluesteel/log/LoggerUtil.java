package com.yilnz.bluesteel.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 动态日志工具
 */
@Slf4j
public class LoggerUtil {

    /**
     * 生成一个时间滚动日志
     * <springProperty scope="context" name="LOG_HOME" source="logback.loghome"/>
     * <property scope="context" name="LOG_HOME" value="/taojin/log"/>
     * <property scope="context" name="MAX_HISTORY" value="7"/>
     * @param name 日志名称
     * @param addConsole 是否添加到控制台
     * @return 日志者
     */
    public static org.slf4j.Logger createTimeBasedRollingFileLogger(String name, boolean addConsole) {
        Logger templateLogger = (Logger) LoggerFactory.getLogger(LoggerUtil.class);
        LoggerContext loggerContext = templateLogger.getLoggerContext();
        org.slf4j.Logger logger = createTimeBasedRollingFileLogger(name, addConsole, loggerContext.getProperty("LOG_HOME"), Integer.parseInt(loggerContext.getProperty("MAX_HISTORY")));
        return logger;
    }

    /**
     * 创建基于时间滚动日志文件
     *
     * @param name       的名字
     * @param addConsole 添加控制台
     * @param logHome    日志回家
     * @param maxHistory 马克思的历史
     * @return {@link Logger }
     * @author zyl
     * @date 2020/11/04
     */
    public static org.slf4j.Logger createTimeBasedRollingFileLogger(String name, boolean addConsole, String logHome, Integer maxHistory){
        Logger logger = null;
        try {
            Logger templateLogger = (Logger) LoggerFactory.getLogger(LoggerUtil.class);
            LoggerContext loggerContext = templateLogger.getLoggerContext();

            RollingFileAppender rollingFileAppender = new RollingFileAppender();

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(loggerContext);
            encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} -%5p [%t] [%X{requestId}] %-40.40logger{39} %L : %m%n -%wEx");
            encoder.setCharset(StandardCharsets.UTF_8);
            encoder.start();

            TimeBasedRollingPolicy policy = new TimeBasedRollingPolicy();
            policy.setContext(loggerContext);
            policy.setFileNamePattern(logHome + "/" + name + "-%d{yyyy-MM-dd}.log");
            policy.setMaxHistory(maxHistory);
            policy.setParent(rollingFileAppender);
            policy.start();

            ThresholdFilter filter = new ThresholdFilter();
            filter.setContext(loggerContext);
            filter.setLevel(Level.INFO.levelStr);
            filter.start();

            rollingFileAppender.setContext(loggerContext);
            rollingFileAppender.setFile(logHome + "/" + name + ".log");
            rollingFileAppender.setRollingPolicy(policy);
            rollingFileAppender.setName(name);
            rollingFileAppender.setEncoder(encoder);
            rollingFileAppender.addFilter(filter);
            rollingFileAppender.start();

            logger = loggerContext.getLogger(name);
            ConsoleAppender console = (ConsoleAppender) loggerContext.getLogger("root").getAppender("CONSOLE");
            if(console == null){
                console = (ConsoleAppender) loggerContext.getLogger("root").getAppender("console");
                if(console == null) {
                    console = new ConsoleAppender();
                    console.setContext(loggerContext);
                    console.setEncoder(encoder);
                    console.addFilter(filter);
                    console.start();
                }
            }
            logger.addAppender(rollingFileAppender);
            if(addConsole){
                logger.addAppender(console);
            }
            logger.setAdditive(false);
        } catch (Exception e) {
            log.error("create log error", e);
        }
        if(logger == null){
            return LoggerFactory.getLogger(name);
        }
        return logger;
    }
}
