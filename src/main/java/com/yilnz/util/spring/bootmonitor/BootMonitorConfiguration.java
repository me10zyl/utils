package com.yilnz.util.spring.bootmonitor;

import com.yilnz.util.spring.quickconf.ThymeleafConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@ComponentScan(value = "com.yilnz.util.spring.controller")
@ImportResource("classpath:spring-beans.xml")
public class BootMonitorConfiguration {

    @Bean
    public SpringBootMonitor springBootMonitor(){
        return new SpringBootMonitor();
    }

}
