package com.frogdevelopment.nihongo.export;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

public class PathExportManager {

    static final String EXPORT_FORMAT = "%s.json";

    private final String exportPath;

    public PathExportManager(final String exportPath) {
        this.exportPath = exportPath;
    }

    @PostConstruct
    private void initExportDirectories() throws IOException {
        Files.createDirectories(Paths.get(exportPath));
    }

    void clearExportDirectories() throws IOException {
        FileUtils.cleanDirectory(Paths.get(exportPath).toFile());
    }

    Path getPathForLang(final String lang) {
        final var fileName = String.format(EXPORT_FORMAT, lang);
        return Paths.get(exportPath, fileName);
    }
}
