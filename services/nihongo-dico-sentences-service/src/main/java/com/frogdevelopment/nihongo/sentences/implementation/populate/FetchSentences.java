package com.frogdevelopment.nihongo.sentences.implementation.populate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import org.apache.commons.lang3.time.StopWatch;
import com.frogdevelopment.nihongo.sentences.implementation.about.AboutDao;
import com.frogdevelopment.nihongo.sentences.implementation.export.ExportSentences;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Singleton
@Transactional
@RequiredArgsConstructor
public class FetchSentences {

    private final PopulateDatabase populateDatabase;
    private final AboutDao aboutDao;
    private final DeleteDownloadedFiles deleteDownloadedFiles;
    private final ExportSentences exportSentences;

    @Async
    public void call() {
        final var stopWatch = new StopWatch();
        log.info(">>>>>>>>>> Start fetching sentences");
        stopWatch.start();
        try {
            final var data = populateDatabase.call();
            aboutDao.generate(data);
            deleteDownloadedFiles.call();
            exportSentences.call();
        } finally {
            stopWatch.stop();
            log.info("<<<<<<<<<< Finish fetching on {}s", stopWatch.getTime(SECONDS));
        }
    }
}
