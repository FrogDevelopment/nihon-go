package fr.frogdevelopment.nihongo.sentence.implementation.populate;

import static fr.frogdevelopment.nihongo.sentence.implementation.populate.utils.FileUtils.getFileName;
import static fr.frogdevelopment.nihongo.sentence.implementation.populate.utils.FileUtils.getTemporaryFile;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.exists;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DownloadFile {

    Path call(String url) throws IOException, URISyntaxException {
        var fileName = getFileName(url);

        var path = getTemporaryFile(fileName);
        if (exists(path)) {
            log.info("- file {} already exists => no need to downloading it", fileName);
        } else {
            log.info("- file {} not present, retrieve it from {}", fileName, url);
            try ( // https://stackoverflow.com/a/18431514/244911 => handle redirection
                    var httpClient = HttpClients.createDefault();
                    var response = httpClient.execute(new HttpGet(url));
                    var inputStream = response.getEntity().getContent()) {
                copy(inputStream, path);
            }
        }

        return path;
    }


}
