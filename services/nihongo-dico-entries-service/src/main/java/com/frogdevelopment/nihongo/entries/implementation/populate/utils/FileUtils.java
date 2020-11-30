package com.frogdevelopment.nihongo.entries.implementation.populate.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.notExists;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class FileUtils {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    public static Path downloadFromUrl(final String url) throws IOException, URISyntaxException {
        final var fileName = getFileName(url);
        final var path = getTemporaryFile(fileName);
        if (exists(path)) {
            log.info("- file {} exists => no need to downloading it", fileName);
        } else {
            log.info("- file {} not present, retrieve it from {}", fileName, url);
            final var urlConnection = new URL(url).openConnection();
            try (final var inputStream = urlConnection.getInputStream()) {
                copy(inputStream, path);
            }
        }

        return path;
    }

    public static void scan(final Path filePath, final Consumer<Scanner> reader) throws IOException {
        try (final var gzInput = new GzipCompressorInputStream(newInputStream(filePath));
             final var inputReader = new InputStreamReader(gzInput, UTF_8);
             final var scanner = new Scanner(inputReader)) {

            reader.accept(scanner);
        }
    }

    public static Path getTemporaryFile(final String fileName) throws IOException {
        final var tmp = getTemporaryDownloadDirectory();

        return Paths.get(tmp.toString(), fileName);
    }

    public static Path getTemporaryDownloadDirectory() throws IOException {
        final var tmp = Paths.get(TMP_DIR, "entries");
        if (notExists(tmp)) {
            createDirectories(tmp);
        }
        return tmp;
    }

    public static void createDirectory(final String uri) throws IOException {
        final var path = Paths.get(uri);
        if (notExists(path)) {
            log.info("Creating path" + uri);
            createDirectories(path);
        }
    }

    private static String getFileName(final String url) throws URISyntaxException {
        final var uri = new URI(url);
        final var path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

}
