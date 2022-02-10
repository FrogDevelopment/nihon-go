package com.frogdevelopment.nihongo.entries.implementation.populate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.lang3.time.StopWatch;
import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import com.frogdevelopment.nihongo.entries.implementation.export.ExportEntries;
import com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.FetchJMDict;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class FetchEntries {

    private final FetchJMDict fetchJmDict;
    private final SaveData saveData;
    private final AboutDao aboutDao;
    private final DeleteDownloadedFiles deleteDownloadedFiles;
    private final ExportEntries exportEntries;

    @Async
    public void call() {
        final var stopWatch = new StopWatch();
        log.info(">>>>>>>>>> Start fetching entries");
        stopWatch.start();
        try {
            final var date = fetchJmDict.execute();
            final var map = saveData.call();
            aboutDao.insert(date, map);
            deleteDownloadedFiles.call();
            exportEntries.call();
        } catch (final IOException | URISyntaxException e) {
            throw new IllegalStateException(e);
        } finally {
            stopWatch.stop();
            log.info("<<<<<<<<<< Finish fetching on {}s", stopWatch.getTime(SECONDS));
        }
    }
}
