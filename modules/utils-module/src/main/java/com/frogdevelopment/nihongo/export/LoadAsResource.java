package com.frogdevelopment.nihongo.export;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
public final class LoadAsResource {

    private final PathExportManager pathExportManager;
    private final FileSystemResourceLoader fileSystemResourceLoader;

    public Resource call(final String lang) {
        final var path = pathExportManager.getPathForLang(lang);
        if (path.toFile().exists()) {
            return fileSystemResourceLoader.getResource("file:" + path.toFile().getPath());
        } else {
            throw new ExportNotFoundException(lang);
        }
    }

}
