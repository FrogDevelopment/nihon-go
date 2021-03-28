package com.frogdevelopment.nihongo.entries.implementation.export;

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
public class ExportEntries {

    @Language("SQL")
    private static final String SQL_COPY = """
            COPY (SELECT f.entry_seq,
                               f.kanji,
                               f.kana,
                               f.reading,
                               f.sense_seq,
                               f.pos,
                               f.field,
                               f.misc,
                               f.info,
                               f.dial,
                               f.vocabulary
                FROM (SELECT e.entry_seq,
                             e.kanji,
                             e.kana,
                             e.reading,
                             s.sense_seq,
                             s.pos,
                             s.field,
                             s.misc,
                             s.info,
                             s.dial,
                             ARRAY_AGG(g.vocabulary) AS vocabulary
                      FROM jpn.entries e
                               INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq
                               INNER JOIN %s.glosses g ON g.sense_seq = s.sense_seq
                      GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq, s.pos, s.field, s.misc, s.info, s.dial) f)
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final ExportData exportData;

    public void call() {
        exportData.call(Arrays.stream(values()).map(language -> of(language.getCode() + "-entries", SQL_COPY.formatted(language.getCode()))));
    }

}
