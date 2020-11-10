package fr.frogdevelopment.nihongo.sentence.implementation.populate;

import static fr.frogdevelopment.nihongo.sentence.implementation.populate.utils.FileUtils.getTemporaryDownloadDirectory;
import static org.apache.commons.io.FileUtils.cleanDirectory;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class DeleteDownloadedFiles {

    void call() {
        log.info("****** Deleting downloaded files");
        try {
            var path = getTemporaryDownloadDirectory();
            cleanDirectory(path.toFile());
        } catch (IOException e) {
            log.error("Error while deleting downloaded files", e);
        }
    }

}
