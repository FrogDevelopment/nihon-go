package com.frogdevelopment.nihongo.export;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidAnswer2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.frogdevelopment.nihongo.multischema.Language.ENG;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

//@Transactional
//@SpringBootTest
//@Tag("integrationTest")
//@ActiveProfiles("test")
class ExportByLangTest {

    @Autowired
    private ExportByLang exportByLang;
    @MockBean
    private PathExportManager pathExportManager;
    @MockBean
    private ExportDao exportDao;

    @Test
    void shouldExportForGivenLang(@TempDir Path tempDir) {
        // given
        given(pathExportManager
                .getPathForLang(ENG.getCode()))
                .willReturn(tempDir);

        doAnswer(answerVoid((VoidAnswer2<String, ResultSetExtractor<Void>>) (lang, rse) -> {
            var iterator = List.of(
                    "{line_1}",
                    "{line_2}",
                    "{line_3}",
                    "{line_4}"
            ).iterator();

            ResultSet resultSet = mock(ResultSet.class);
            given(resultSet.getString(1)).willReturn(iterator.next());
            given(resultSet.next()).willReturn(iterator.hasNext());

            rse.extractData(resultSet);
        })).when(exportDao).export(anyString(), any());

        // when
        exportByLang.call(ENG);

        // then

    }


}
