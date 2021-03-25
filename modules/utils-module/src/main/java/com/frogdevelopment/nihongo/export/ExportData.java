package com.frogdevelopment.nihongo.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Function;

import org.springframework.core.io.FileSystemResourceLoader;

@Slf4j
@RequiredArgsConstructor
public final class ExportData {

    private final PathExportManager pathExportManager;
    private final FileSystemResourceLoader fileSystemResourceLoader;
    private final CopyOut copyOut;

    public void call(final Function<String, String> copySqlSupplier) {
        try {
            log.info("****** Clear export resources");
            fileSystemResourceLoader.clearResourceCaches();
            pathExportManager.clearExportDirectories();

            log.info("****** Export data");
            copyOut.call(copySqlSupplier);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        log.info("****** Export done");
    }
}
