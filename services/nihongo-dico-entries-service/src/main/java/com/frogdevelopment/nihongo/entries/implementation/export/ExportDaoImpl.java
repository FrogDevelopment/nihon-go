package com.frogdevelopment.nihongo.entries.implementation.export;

import com.frogdevelopment.nihongo.export.ExportDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ExportDaoImpl implements ExportDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void export(final String lang, final ResultSetExtractor<Void> rse) {
        @Language("SQL") final var sql = """
                SELECT JSON_BUILD_OBJECT(
                               'entry_seq', f.entry_seq,
                               'kanji', f.kanji,
                               'kana', f.kana,
                               'reading', f.reading,
                               'sense_seq', f.sense_seq,
                               'pos', f.pos,
                               'field', f.field,
                               'misc', f.misc,
                               'info', f.info,
                               'dial', f.dial,
                               'vocabularies', f.vocabulary
                           )
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
                      GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq, s.pos, s.field, s.misc, s.info, s.dial) f
                """;

        jdbcTemplate.query(String.format(sql, lang), rse);
    }
}
