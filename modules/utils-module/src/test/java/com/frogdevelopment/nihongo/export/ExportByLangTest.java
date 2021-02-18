package com.frogdevelopment.nihongo.export;

import static com.frogdevelopment.nihongo.multischema.Language.ENG;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.doAnswer;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.stream.IntStream;

import org.intellij.lang.annotations.Language;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.VoidAnswer2;
import org.springframework.jdbc.core.ResultSetExtractor;

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
    void shouldExportForGivenLang(@TempDir Path tempDir) throws IOException, JSONException {
        // given
        var exportFile = Paths.get(tempDir.toString(), "export.json");
        given(pathExportManager
                .getPathForLang(ENG.getCode()))
                .willReturn(exportFile);

        var nbItems = 10;
        doAnswer(answerVoid((VoidAnswer2<String, ResultSetExtractor<Void>>) (lang, rse) -> {
            var iterator = IntStream.range(0, nbItems)
                    .mapToObj(count -> generateItem(lang, count))
                    .iterator();
            ResultSet resultSet = mock(ResultSet.class);
            given(resultSet.getString(1)).will(invocation -> iterator.next());
            given(resultSet.next()).will(invocation -> iterator.hasNext());

            rse.extractData(resultSet);
        })).when(exportDao).export(anyString(), any());

        // when
        exportByLang.call(ENG);

        // then
        var allLines = readAllLines(exportFile, UTF_8);
        assertThat(allLines).hasSize(1);

        var jsonArray = new JSONArray(allLines.get(0));
        assertThat(jsonArray.length()).isEqualTo(nbItems);
        for (int value = 0; value < nbItems; value++) {
            assertThat(jsonArray.getJSONObject(value).getString("lang")).isEqualTo(ENG.getCode());
            assertThat(jsonArray.getJSONObject(value).getString("item")).isEqualTo(String.valueOf(value));
            assertThat(jsonArray.getJSONObject(value).getString("data")).isEqualTo("some data for " + ENG.getCode());
        }
    }

    private String generateItem(String lang, int count) {
        @Language("JSON")
        String json = """
                {"lang": "%1$s","item": "%2$s","data": "some data for %1$s"}""".formatted(lang, count);
        return json;
    }


}
