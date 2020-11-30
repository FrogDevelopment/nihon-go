package com.frogdevelopment.nihongo.export;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;

@Configuration
public class ExportConfiguration {

    @Bean
    FileSystemResourceLoader fileSystemResourceLoader() {
        return new FileSystemResourceLoader();
    }

    @Bean
    public PathExportManager pathExportManager(@Value("${frog.export.path:/opt/export}") final String exportPath) {
        return new PathExportManager(exportPath);
    }

    @Bean
    public LoadAsResource loadAsResource(final PathExportManager pathExportManager,
                                         final FileSystemResourceLoader fileSystemResourceLoader) {
        return new LoadAsResource(pathExportManager, fileSystemResourceLoader);
    }

    @Bean
    public ExportData exportData(final PathExportManager pathExportManager,
                                 final FileSystemResourceLoader fileSystemResourceLoader,
                                 final ExportByLang exportByLang) {
        return new ExportData(pathExportManager, fileSystemResourceLoader, exportByLang);
    }

    @Bean
    public ExportByLang exportByLang(final PathExportManager pathExportManager,
                                     final ExportDao exportDao) {
        return new ExportByLang(pathExportManager, exportDao);
    }
}
