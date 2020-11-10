package fr.frogdevelopment.nihongo.entries.implementation.export;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResourceLoader;

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
    void shouldClearResourceCacheThenExportForEachLang() {
        // given
        given(exportDao.getLanguages())
                .willReturn(List.of("lang_1", "lang_2", "lang_3"));

        // when
        exportByLang.call();

        // then
        then(fileSystemResourceLoader)
                .should()
                .clearResourceCaches();
        then(exportDao)
                .should(times(3))
                .export(anyString());
    }
}
