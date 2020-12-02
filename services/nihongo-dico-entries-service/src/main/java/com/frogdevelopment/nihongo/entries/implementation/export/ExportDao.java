package com.frogdevelopment.nihongo.entries.implementation.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ExportDao {

    private final JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean export(String lang) {
        var sql = "SELECT JSON_BUILD_OBJECT("
                + "               'entry_seq', f.entry_seq,"
                + "               'kanji', f.kanji,"
                + "               'kana', f.kana,"
                + "               'reading', f.reading,"
                + "               'sense_seq', f.sense_seq,"
                + "               'pos', f.pos,"
                + "               'field', f.field,"
                + "               'misc', f.misc,"
                + "               'info', f.info,"
                + "               'dial', f.dial,"
                + "               'vocabularies', f.vocabulary"
                + "           )"
                + "FROM (SELECT e.entry_seq,"
                + "             e.kanji,"
                + "             e.kana,"
                + "             e.reading,"
                + "             s.sense_seq,"
                + "             s.pos,"
                + "             s.field,"
                + "             s.misc,"
                + "             s.info,"
                + "             s.dial,"
                + "             ARRAY_AGG(g.vocabulary) AS vocabulary"
                + "      FROM jpn.entries e"
                + "               INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq"
                + "               INNER JOIN " + lang + ".glosses g ON g.sense_seq = s.sense_seq "
                + "      GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq, s.pos, s.field, s.misc, s.info, s.dial) f";

        return jdbcTemplate.query(sql, ExportWriter.exportToFile(lang));
    }
}