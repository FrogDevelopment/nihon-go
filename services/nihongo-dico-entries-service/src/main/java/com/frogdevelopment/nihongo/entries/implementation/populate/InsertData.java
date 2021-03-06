package com.frogdevelopment.nihongo.entries.implementation.populate;

import static com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getEntries;
import static com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getGlosses;
import static com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getSenses;
import static java.nio.charset.StandardCharsets.UTF_8;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.stereotype.Component;

import com.frogdevelopment.nihongo.multischema.Language;

@Slf4j
@Component
class InsertData {

    @org.intellij.lang.annotations.Language("SQL")
    private static final String COPY_ENTRIES = """
            COPY jpn.entries (entry_seq, kanji, kana, reading) FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')
            """;

    @org.intellij.lang.annotations.Language("SQL")
    private static final String COPY_SENSES = """
            COPY jpn.senses (sense_seq, entry_seq, pos, field, misc, info, dial) FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')
            """;

    @org.intellij.lang.annotations.Language("SQL")
    private static final String COPY_GLOSSES = """
            COPY %s.glosses (sense_seq, vocabulary) FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')
            """;

    Map<String, Object> call(final Connection connection) throws IOException, SQLException {
        final var pgConnection = connection.unwrap(PGConnection.class);
        final var copyManager = pgConnection.getCopyAPI();

        final var map = new HashMap<String, Object>();
        log.info("- insert entries");
        map.put("nb_entries", copyIn(copyManager, COPY_ENTRIES, getEntries()));

        log.info("- insert senses");
        copyIn(copyManager, COPY_SENSES, getSenses());

        final var mapLanguages = new HashMap<String, Long>();
        for (final Language language : Language.values()) {
            log.info("- insert glosses for language {}", language);
            final var code = language.getCode();
            mapLanguages.put(code, copyIn(copyManager, String.format(COPY_GLOSSES, code), getGlosses(code)));
        }
        map.put("languages", mapLanguages);

        return map;
    }

    private static long copyIn(final CopyManager copyManager, final String sql, final Path path) throws IOException, SQLException {
        try (final var in = new BufferedReader(new FileReader(path.toFile(), UTF_8))) {
            final var nbInserted = copyManager.copyIn(sql, in);
            log.info("\t{} lines inserted", nbInserted);
            return nbInserted;
        }
    }
}
