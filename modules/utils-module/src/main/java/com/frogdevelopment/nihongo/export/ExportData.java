package com.frogdevelopment.nihongo.export;

import com.frogdevelopment.nihongo.multischema.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResourceLoader;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public final class ExportData {

    private final PathExportManager pathExportManager;
    private final FileSystemResourceLoader fileSystemResourceLoader;
    private final ExportByLang exportByLang;

    public void call() throws IOException {
        fileSystemResourceLoader.clearResourceCaches();
        pathExportManager.clearExportDirectories();

        Arrays.stream(Language.values())
                .parallel()
                .forEach(exportByLang::call);
    }
}
