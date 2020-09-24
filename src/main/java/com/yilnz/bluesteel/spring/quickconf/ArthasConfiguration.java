package com.yilnz.bluesteel.spring.quickconf;

import com.yilnz.bluesteel.arthas.ArthasInbound;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ArthasConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ArthasInbound.startArthas();
    }
}
