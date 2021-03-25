package com.frogdevelopment.nihongo.export;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

@RequiredArgsConstructor
public class PathExportManager {

    private static final String EXPORT_FORMAT = "%s.json";

    private final String exportPath;

    @PostConstruct
    void initExportDirectories() throws IOException {
        Files.createDirectories(Paths.get(exportPath));
    }

    void clearExportDirectories() throws IOException {
        FileUtils.cleanDirectory(Paths.get(exportPath).toFile());
    }

    public Path getPathForLang(final String lang) {
        final var fileName = String.format(EXPORT_FORMAT, lang);
        return Paths.get(exportPath, fileName);
    }
}
