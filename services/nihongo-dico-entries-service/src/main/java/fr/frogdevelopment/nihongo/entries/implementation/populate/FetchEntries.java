package fr.frogdevelopment.nihongo.entries.implementation.populate;

import static java.util.concurrent.TimeUnit.SECONDS;

import fr.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import fr.frogdevelopment.nihongo.entries.implementation.export.ExportByLang;
import fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.FetchJMDict;
import java.io.IOException;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FetchEntries {

    private final FetchJMDict fetchJmDict;
    private final SaveData saveData;
    private final AboutDao aboutDao;
    private final DeleteDownloadedFiles deleteDownloadedFiles;
    private final ExportByLang exportByLang;

    public FetchEntries(FetchJMDict fetchJmDict,
                        SaveData saveData,
                        AboutDao aboutDao,
                        DeleteDownloadedFiles deleteDownloadedFiles,
                        ExportByLang exportByLang) {
        this.fetchJmDict = fetchJmDict;
        this.saveData = saveData;
        this.aboutDao = aboutDao;
        this.deleteDownloadedFiles = deleteDownloadedFiles;
        this.exportByLang = exportByLang;
    }

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
