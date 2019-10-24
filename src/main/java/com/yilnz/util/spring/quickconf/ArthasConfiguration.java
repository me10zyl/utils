package com.yilnz.util.spring.quickconf;

import com.yilnz.util.arthas.ArthasInbound;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ArthasConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ArthasInbound.startArthas();
    }
}
