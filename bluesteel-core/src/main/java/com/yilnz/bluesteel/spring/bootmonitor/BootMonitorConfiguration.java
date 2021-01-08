package com.yilnz.bluesteel.spring.bootmonitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@ComponentScan(value = "com.yilnz.bluesteel.spring.controller")
@ImportResource("classpath:spring-beans.xml")
public class BootMonitorConfiguration {

    @Bean
    public SpringBootMonitor springBootMonitor(){
        return new SpringBootMonitor();
    }

}
