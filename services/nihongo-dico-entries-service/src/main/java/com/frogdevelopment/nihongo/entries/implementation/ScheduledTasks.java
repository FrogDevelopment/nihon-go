package com.frogdevelopment.nihongo.entries.implementation;

import com.frogdevelopment.nihongo.entries.implementation.search.Search;
import com.frogdevelopment.nihongo.multischema.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!local")
@RequiredArgsConstructor
public class ScheduledTasks {

    private final Search search;

    @Scheduled(cron = "0 * * * * *")
    public void wakeUpPostgres() {
        log.info("Scheduled task wakeUpPostgres");
        search.call(Language.ENG.getCode(), "eat");
    }
}

