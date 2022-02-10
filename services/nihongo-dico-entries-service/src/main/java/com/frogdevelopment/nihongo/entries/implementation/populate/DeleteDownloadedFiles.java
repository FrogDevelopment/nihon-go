package com.frogdevelopment.nihongo.entries.implementation.populate;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.context.scope.Refreshable;
import jakarta.inject.Singleton;

import static com.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils.getTemporaryDownloadDirectory;
import static org.apache.commons.io.FileUtils.cleanDirectory;

@Slf4j
@Singleton
@Refreshable
class DeleteDownloadedFiles {

    private final boolean clean;

    DeleteDownloadedFiles(@Value("${frog.clean:true}") final boolean clean) {
        this.clean = clean;
    }

    void call() {
        if (clean) {
            log.info("****** Deleting downloaded files");
            try {
                final var path = getTemporaryDownloadDirectory();
                cleanDirectory(path.toFile());
            } catch (final IOException e) {
                log.error("Error while deleting downloaded files", e);
            }
        }
    }

}
