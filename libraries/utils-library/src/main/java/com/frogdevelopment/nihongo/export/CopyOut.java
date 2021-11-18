package com.frogdevelopment.nihongo.export;

import static com.frogdevelopment.nihongo.export.ExportData.Export;
import static java.nio.charset.StandardCharsets.UTF_8;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import org.postgresql.copy.CopyManager;

@Slf4j
@RequiredArgsConstructor
class CopyOut {

    private final CompressExport compressExport;

    Path call(final CopyManager copyManager, final Export export) {
        return compressExport.call(export, path -> {
            final File file = path.toFile();
            try (final var fileWriter = new BufferedWriter(new FileWriter(file, UTF_8))) {
                copyManager.copyOut(export.getCopySql(), fileWriter);
            }
        });
    }

}
