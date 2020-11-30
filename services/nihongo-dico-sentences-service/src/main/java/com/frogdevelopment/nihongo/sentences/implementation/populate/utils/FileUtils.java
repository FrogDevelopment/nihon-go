package com.frogdevelopment.nihongo.sentences.implementation.populate.utils;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.notExists;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class FileUtils {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    public static String getFileName(final String url) throws URISyntaxException {
        final var uri = new URI(url);
        final var path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static Path getTemporaryFile(final String fileName) throws IOException {
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
