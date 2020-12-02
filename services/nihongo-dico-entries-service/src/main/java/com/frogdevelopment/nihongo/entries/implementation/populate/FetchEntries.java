package com.frogdevelopment.nihongo.entries.implementation.populate;

import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import com.frogdevelopment.nihongo.entries.implementation.export.ExportByLang;
import com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.FetchJMDict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Component
@RequiredArgsConstructor
public class FetchEntries {

    private final FetchJMDict fetchJmDict;
    private final SaveData saveData;
    private final AboutDao aboutDao;
    private final DeleteDownloadedFiles deleteDownloadedFiles;
    private final ExportByLang exportByLang;

    @Async
    public void call() {
        var stopWatch = new StopWatch();
        log.info(">>>>>>>>>> Start fetching entries");
        stopWatch.start();
        try {
            var date = fetchJmDict.execute();
            saveData.call();
            aboutDao.insert(date);
            deleteDownloadedFiles.call();
            exportByLang.call();
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException(e);
        } finally {
            stopWatch.stop();
            log.info("<<<<<<<<<< Finish fetching on {}s", stopWatch.getTime(SECONDS));
        }
    }
}
