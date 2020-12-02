package com.frogdevelopment.nihongo.entries.implementation.populate;

import com.frogdevelopment.nihongo.entries.implementation.Language;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import static com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getEntries;
import static com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getGlosses;
import static com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getSenses;

@Slf4j
@Component
class InsertData {

    private static final String COPY_ENTRIES = "COPY jpn.entries (entry_seq, kanji, kana, reading)"
            + " FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')";

    private static final String COPY_SENSES = "COPY jpn.senses (sense_seq, entry_seq, pos, field, misc, info, dial)"
            + " FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')";

    private static final String COPY_GLOSSES = "COPY glosses (sense_seq, lang, vocabulary)"
            + " FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')";

    void call(Connection connection) throws IOException, SQLException {
        var pgConnection = connection.unwrap(PGConnection.class);
        var copyManager = pgConnection.getCopyAPI();

        log.info("- insert entries");
        copyIn(copyManager, COPY_ENTRIES, getEntries());
        log.info("- insert senses");
        copyIn(copyManager, COPY_SENSES, getSenses());
        Arrays.stream(Language.values()).parallel().forEach(language -> {
            log.info("- insert glosses for language {}", language);
            copyIn(copyManager, COPY_GLOSSES, getGlosses(language.getCode()));
        });
    }

    private static void copyIn(CopyManager copyManager, String sql, Path path) {
        try (var in = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            var nbInserted = copyManager.copyIn(sql, in);
            log.info("\t{} lines inserted", nbInserted);
        } catch (Exception e) {
            throw new RuntimeException(e); // fixme dedicated exception
        }
    }
}
