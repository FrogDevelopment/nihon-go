package com.frogdevelopment.nihongo.entries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Profile("!test")
public class StartupApplicationListener {

    @EventListener
    public void onApplicationStarted(@NonNull ContextRefreshedEvent event) throws IOException {
//        FileUtils.createDirectory("/export");
    }
}
