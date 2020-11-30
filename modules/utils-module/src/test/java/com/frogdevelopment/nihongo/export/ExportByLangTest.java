package com.frogdevelopment.nihongo.export;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.frogdevelopment.nihongo.multischema.Language.ENG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class ExportByLangTest {

    @InjectMocks
    private ExportByLang exportByLang;
    @Mock
    private PathExportManager pathExportManager;
    @Mock
    private ExportDao exportDao;

    @Test
    void shouldExportForGivenLang() {
        // when
        exportByLang.call(ENG);

        // then
        then(exportDao)
                .should()
                .export(eq(ENG.getCode()), any());
//        then(pathExportManager)
//                .should()
//                .getPathForLang(ENG.getCode());
    }
}