package com.frogdevelopment.nihongo.entries.implementation.export;

import com.frogdevelopment.nihongo.entries.implementation.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ExportByLang {

    private final FileSystemResourceLoader fileSystemResourceLoader;
    private final ExportDao exportDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void call() throws IOException {
        fileSystemResourceLoader.clearResourceCaches();
        ExportWriter.initExportDirectories();

        Arrays.stream(Language.values())
                .parallel()
                .map(Language::getCode)
                .forEach(exportDao::export);
    }
}
