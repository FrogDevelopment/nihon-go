package com.frogdevelopment.nihongo.sentences.implementation.export;

import com.frogdevelopment.nihongo.export.ExportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.frogdevelopment.nihongo.export.ExportData.Export.of;
import static com.frogdevelopment.nihongo.multischema.Language.values;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportSentences {

    @Language("SQL")
    private static final String SQL_COPY = """
            COPY (SELECT i.linking, japanese.sentence AS japanese, translation.sentence AS translation
                  FROM jpn.sentences japanese
                           INNER JOIN %1$s.links_japanese_translation links ON links.japanese_id = japanese.sentence_id
                           INNER JOIN %1$s.sentences translation ON translation.sentence_id = links.translation_id
                           INNER JOIN jpn.japanese_indices i ON i.japanese_id = japanese.sentence_id
                  )
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final ExportData exportData;

    public void call() {
        exportData.call(Arrays.stream(values()).map(language -> of(language.getCode() + "_sentences", SQL_COPY.formatted(language.getCode()))));
    }

}
