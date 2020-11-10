package fr.frogdevelopment.nihongo.entries.implementation.export;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class ExportDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExportDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> getLanguages() {
        String sql = "SELECT DISTINCT g.lang FROM glosses g";
        return namedParameterJdbcTemplate.getJdbcTemplate().queryForList(sql, String.class);
    }

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
                  + "      FROM entries e"
                  + "               INNER JOIN senses s ON e.entry_seq = s.entry_seq"
                  + "               INNER JOIN glosses g ON g.sense_seq = s.sense_seq AND g.lang = :lang"
                  + "      GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq, s.pos, s.field, s.misc, s.info, s.dial) f";

        var params = new MapSqlParameterSource("lang", lang);
        return namedParameterJdbcTemplate.query(sql, params, ExportWriter.exportToFile(lang));
    }
}
