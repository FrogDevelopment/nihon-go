package com.frogdevelopment.nihongo.entries.implementation.export;

import com.frogdevelopment.nihongo.multischema.Language;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResourceLoader;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class ExportByLangTest {

    @InjectMocks
    private ExportByLang exportByLang;
    @Mock
    private FileSystemResourceLoader fileSystemResourceLoader;
    @Mock
    private ExportDao exportDao;

    @Test
    void shouldClearResourceCacheThenExportForEachLang() throws IOException {
        // when
        exportByLang.call();

        // then
        then(fileSystemResourceLoader)
                .should()
                .clearResourceCaches();
        then(exportDao)
                .should(times(Language.values().length))
                .export(anyString());
    }
}
