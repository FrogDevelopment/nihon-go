package com.frogdevelopment.nihongo.sentences.implementation.populate;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import com.frogdevelopment.nihongo.sentences.implementation.populate.utils.FileUtils;

import jakarta.inject.Singleton;

import static org.apache.commons.io.FileUtils.cleanDirectory;

@Slf4j
@Singleton
class DeleteDownloadedFiles {

    void call() {
        log.info("****** Deleting downloaded files");
        try {
            final var path = FileUtils.getTemporaryDownloadDirectory();
            cleanDirectory(path.toFile());
        } catch (final IOException e) {
            log.error("Error while deleting downloaded files", e);
        }
    }

}
