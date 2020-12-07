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
import java.util.HashMap;
import java.util.Map;

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

    private static final String COPY_GLOSSES = "COPY %s.glosses (sense_seq, vocabulary)"
            + " FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')";

    Map<String, Object> call(Connection connection) throws IOException, SQLException {
        var pgConnection = connection.unwrap(PGConnection.class);
        var copyManager = pgConnection.getCopyAPI();

        var map = new HashMap<String, Object>();
        log.info("- insert entries");
        map.put("nb_entries", copyIn(copyManager, COPY_ENTRIES, getEntries()));

        log.info("- insert senses");
        copyIn(copyManager, COPY_SENSES, getSenses());

        var mapLanguages = new HashMap<String, Long>();
        for (Language language : Language.values()) {
            log.info("- insert glosses for language {}", language);
            var code = language.getCode();
            mapLanguages.put(code, copyIn(copyManager, String.format(COPY_GLOSSES, code), getGlosses(code)));
        }
        map.put("languages", mapLanguages);

        return map;
    }

    private static long copyIn(CopyManager copyManager, String sql, Path path) throws IOException, SQLException {
        try (var in = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            var nbInserted = copyManager.copyIn(sql, in);
            log.info("\t{} lines inserted", nbInserted);
            return nbInserted;
        }
    }
}
