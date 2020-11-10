package fr.frogdevelopment.nihongo.entries.implementation.populate.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.notExists;
import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Consumer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class FileUtils {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    public static Path downloadFromUrl(String url) throws IOException, URISyntaxException {
        var fileName = getFileName(url);
        var path = getTemporaryFile(fileName);
        if (exists(path)) {
            log.info("- file {} exists => no need to downloading it", fileName);
        } else {
            log.info("- file {} not present, retrieve it from {}", fileName, url);
            var urlConnection = new URL(url).openConnection();
            try (var inputStream = urlConnection.getInputStream()) {
                copy(inputStream, path);
            }
        }

        return path;
    }

    public static void scan(Path filePath, Consumer<Scanner> reader) throws IOException {
        try (var gzInput = new GzipCompressorInputStream(newInputStream(filePath));
                var inputReader = new InputStreamReader(gzInput, UTF_8);
                var scanner = new Scanner(inputReader)) {

            reader.accept(scanner);
        }
    }

    public static Path getTemporaryFile(String fileName) throws IOException {
        var tmp = getTemporaryDownloadDirectory();

        return Paths.get(tmp.toString(), fileName);
    }

    public static Path getTemporaryDownloadDirectory() throws IOException {
        var tmp = Paths.get(TMP_DIR, "entries");
        if (notExists(tmp)) {
            createDirectories(tmp);
        }
        return tmp;
    }

    public static void createDirectory(String uri) throws IOException {
        var path = Paths.get(uri);
        if (notExists(path)) {
            log.info("Creating path" + uri);
            createDirectories(path);
        }
    }

    private static String getFileName(String url) throws URISyntaxException {
        var uri = new URI(url);
        var path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

}
