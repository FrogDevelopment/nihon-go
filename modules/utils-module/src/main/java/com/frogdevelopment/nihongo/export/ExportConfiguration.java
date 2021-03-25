package com.frogdevelopment.nihongo.export;

import javax.sql.DataSource;

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
    public PathExportManager pathExportManager(@Value("${frog.export.path}") final String exportPath) {
        return new PathExportManager(exportPath);
    }

    @Bean
    public LoadExportAsResource loadAsResource(final PathExportManager pathExportManager,
                                               final FileSystemResourceLoader fileSystemResourceLoader) {
        return new LoadExportAsResource(pathExportManager, fileSystemResourceLoader);
    }

    @Bean
    public CopyOut copyOut(final DataSource dataSource,
                           final PathExportManager pathExportManager) {
        return new CopyOut(dataSource, pathExportManager);
    }

    @Bean
    public ExportData exportData(final PathExportManager pathExportManager,
                                 final FileSystemResourceLoader fileSystemResourceLoader,
                                 final CopyOut copyOut) {
        return new ExportData(pathExportManager, fileSystemResourceLoader, copyOut);
    }

}
