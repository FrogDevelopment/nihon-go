package com.frogdevelopment.nihongo.sentences.implementation.populate;

import com.frogdevelopment.nihongo.sentences.implementation.populate.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.apache.commons.io.FileUtils.cleanDirectory;

@Slf4j
@Component
class DeleteDownloadedFiles {

    void call() {
        log.info("****** Deleting downloaded files");
        try {
            var path = FileUtils.getTemporaryDownloadDirectory();
            cleanDirectory(path.toFile());
        } catch (IOException e) {
            log.error("Error while deleting downloaded files", e);
        }
    }

}
