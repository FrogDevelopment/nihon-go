package fr.frogdevelopment.nihongo.entries.implementation.populate;

import static fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getEntries;
import static fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getGlosses;
import static fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.CsvWriter.getSenses;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class InsertData {

    private static final String COPY_ENTRIES = "COPY entries (entry_seq, kanji, kana, reading)"
                                               + " FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')";

    private static final String COPY_SENSES = "COPY senses (sense_seq, entry_seq, pos, field, misc, info, dial)"
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
        log.info("- insert glosses");
        copyIn(copyManager, COPY_GLOSSES, getGlosses());
    }

    private static void copyIn(CopyManager copyManager, String sql, Path path) throws IOException, SQLException {
        try (var in = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            var nbInserted = copyManager.copyIn(sql, in);
            log.info("\t{} lines inserted", nbInserted);
        }
    }
}
