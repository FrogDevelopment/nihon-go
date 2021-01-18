package fr.frogdevelopment.nihongo.entries.implementation.populate;

import static fr.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils.getTemporaryDownloadDirectory;
import static org.apache.commons.io.FileUtils.cleanDirectory;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RefreshScope
class DeleteDownloadedFiles {

    private final boolean clean;

    DeleteDownloadedFiles(@Value("${frog.clean:true}") boolean clean) {
        this.clean = clean;
    }

    void call() {
        if (clean) {
            log.info("****** Deleting downloaded files");
            try {
                var path = getTemporaryDownloadDirectory();
                cleanDirectory(path.toFile());
            } catch (IOException e) {
                log.error("Error while deleting downloaded files", e);
            }
        }
    }

}