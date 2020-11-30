package com.frogdevelopment.nihongo.export;

import com.frogdevelopment.nihongo.multischema.Language;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResourceLoader;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class ExportDataTest {

    @InjectMocks
    private ExportData exportData;
    @Mock
    private PathExportManager pathExportManager;
    @Mock
    private FileSystemResourceLoader fileSystemResourceLoader;
    @Mock
    private ExportByLang exportByLang;

    @Test
    void shouldClearResourceCacheThenExportForEachLang() throws IOException {
        // when
        exportData.call();

        // then
        then(fileSystemResourceLoader)
                .should()
                .clearResourceCaches();
        then(pathExportManager)
                .should()
                .clearExportDirectories();
        then(exportByLang)
                .should(times(Language.values().length))
                .call(any(Language.class));
    }
}