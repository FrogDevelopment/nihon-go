package com.frogdevelopment.nihongo.entries.implementation.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import com.frogdevelopment.nihongo.export.DataFtpExporter;

import jakarta.inject.Singleton;

import static com.frogdevelopment.nihongo.Language.values;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class ExportEntries {

    @Language("SQL")
    private static final String SQL_ENTRIES = """
            COPY (SELECT DISTINCT e.entry_seq,
                                  e.kanji,
                                  e.kana,
                                  e.reading
                  FROM entries e
                           INNER JOIN senses s ON e.entry_seq = s.entry_seq
                           INNER JOIN glosses g ON g.sense_seq = s.sense_seq AND g.language = '%s')
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
                  FROM entries e
                           INNER JOIN senses s ON e.entry_seq = s.entry_seq
                           INNER JOIN glosses g ON g.sense_seq = s.sense_seq AND g.language = '%s'
                  GROUP BY e.entry_seq, s.sense_seq, s.pos, s.field, s.misc, s.info, s.dial)
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final DataFtpExporter dataFtpExporter;

    @Transactional
    public void call() {
        final var entriesStream = getExportStream("_entries", SQL_ENTRIES);
        final var sensesStream = getExportStream("_senses", SQL_SENSES);
        dataFtpExporter.export(Stream.concat(entriesStream, sensesStream));
    }

    @NotNull
    private Stream<DataFtpExporter.Export> getExportStream(final String type, final String sql) {
        return Arrays.stream(values()).map(language -> new DataFtpExporter.Export(language.getCode() + type, sql.formatted(language.getCode())));
    }

}
