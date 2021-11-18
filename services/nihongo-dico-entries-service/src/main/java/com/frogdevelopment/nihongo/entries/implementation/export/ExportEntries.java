package com.frogdevelopment.nihongo.entries.implementation.export;

import com.frogdevelopment.nihongo.export.ExportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.frogdevelopment.nihongo.export.ExportData.Export.of;
import static com.frogdevelopment.nihongo.multischema.Language.values;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportEntries {

    @Language("SQL")
    private static final String SQL_ENTRIES = """
            COPY (SELECT DISTINCT e.entry_seq,
                                  e.kanji,
                                  e.kana,
                                  e.reading
                  FROM jpn.entries e
                           INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq
                           INNER JOIN %s.glosses g ON g.sense_seq = s.sense_seq)
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;
    @Language("SQL")
    private static final String SQL_SENSES = """
            COPY (SELECT e.entry_seq,
                         s.sense_seq,
                         s.pos,
                         s.field,
                         s.misc,
                         s.info,
                         s.dial,
                         string_agg(g.vocabulary, ', ') AS vocabulary
                  FROM jpn.entries e
                           INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq
                           INNER JOIN %s.glosses g ON g.sense_seq = s.sense_seq
                  GROUP BY e.entry_seq, s.sense_seq, s.pos, s.field, s.misc, s.info, s.dial)
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final ExportData exportData;

    public void call() {
        final var entriesStream = getExportStream("_entries", SQL_ENTRIES);
        final var sensesStream = getExportStream("_senses", SQL_SENSES);
        exportData.call(Stream.concat(entriesStream, sensesStream));
    }

    @NotNull
    private Stream<ExportData.Export> getExportStream(final String type, final String sql) {
        return Arrays.stream(values()).map(language -> of(language.getCode() + type, sql.formatted(language.getCode())));
    }

}
