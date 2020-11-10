package fr.frogdevelopment.nihongo.sentence.implementation.populate.utils;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.notExists;
import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class FileUtils {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    public static String getFileName(String url) throws URISyntaxException {
        var uri = new URI(url);
        var path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static Path getTemporaryFile(String fileName) throws IOException {
        final Path tmp = getTemporaryDownloadDirectory();

        return Paths.get(tmp.toString(), fileName);
    }

    public static Path getTemporaryDownloadDirectory() throws IOException {
        final Path tmp = Paths.get(TMP_DIR, "sentences");
        if (notExists(tmp)) {
            createDirectories(tmp);
        }
        return tmp;
    }

}
