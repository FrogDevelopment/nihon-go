package fr.frogdevelopment.nihongo.sentence.implementation.populate;

import fr.frogdevelopment.nihongo.sentence.implementation.about.AboutDao;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class FetchSentences {

    private final PopulateDatabase populateDatabase;
    private final AboutDao aboutDao;
    private final DeleteDownloadedFiles deleteDownloadedFiles;

    public FetchSentences(PopulateDatabase populateDatabase,
                          AboutDao aboutDao,
                          DeleteDownloadedFiles deleteDownloadedFiles) {
        this.populateDatabase = populateDatabase;
        this.aboutDao = aboutDao;
        this.deleteDownloadedFiles = deleteDownloadedFiles;
    }

    @Async
    public void call() {
        populateDatabase.call();
        aboutDao.generate();
        deleteDownloadedFiles.call();
    }
}
