package com.frogdevelopment.nihongo.sentences.implementation.populate;

import com.frogdevelopment.nihongo.sentences.implementation.about.AboutDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class FetchSentences {

    private final PopulateDatabase populateDatabase;
    private final AboutDao aboutDao;
    private final DeleteDownloadedFiles deleteDownloadedFiles;

    @Async
    public void call() {
        var stopWatch = new StopWatch();
        log.info(">>>>>>>>>> Start fetching sentences");
        stopWatch.start();
        try {
            var data = populateDatabase.call();
//            aboutDao.generate(data);
//            deleteDownloadedFiles.call();
        } finally {
            stopWatch.stop();
            log.info("<<<<<<<<<< Finish fetching on {}s", stopWatch.getTime(SECONDS));
        }
    }
}
