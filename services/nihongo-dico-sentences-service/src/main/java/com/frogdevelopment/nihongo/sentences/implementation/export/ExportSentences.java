package com.frogdevelopment.nihongo.sentences.implementation.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import org.intellij.lang.annotations.Language;
import com.frogdevelopment.nihongo.export.DataFtpExporter;

import jakarta.inject.Singleton;

import static com.frogdevelopment.nihongo.Language.values;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class ExportSentences {

    @Language("SQL")
    private static final String SQL_COPY = """
            COPY (SELECT i.linking, japanese.sentence AS japanese, translation.sentence AS translation
                  FROM sentences japanese
                           INNER JOIN links_japanese_translation links ON links.japanese_id = japanese.sentence_id
                           INNER JOIN sentences translation ON translation.sentence_id = links.translation_id
                           INNER JOIN japanese_indices i ON i.japanese_id = japanese.sentence_id
                  )
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final DataFtpExporter dataFtpExporter;

    public void call() {
        dataFtpExporter.export(Arrays.stream(values()).map(language -> new DataFtpExporter.Export(language.getCode() + "_sentences", SQL_COPY)));
    }

}
