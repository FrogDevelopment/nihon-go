package fr.frogdevelopment.nihongo.entries.implementation.export;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ExportByLang {

    private final FileSystemResourceLoader fileSystemResourceLoader;
    private final ExportDao exportDao;

    public ExportByLang(FileSystemResourceLoader fileSystemResourceLoader,
                        ExportDao exportDao) {
        this.fileSystemResourceLoader = fileSystemResourceLoader;
        this.exportDao = exportDao;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void call() {
        fileSystemResourceLoader.clearResourceCaches();

        exportDao.getLanguages().forEach(exportDao::export);
    }
}
