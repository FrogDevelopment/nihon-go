package fr.frogdevelopment.nihongo.entries.implementation.search;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import fr.frogdevelopment.nihongo.entries.implementation.search.entity.SearchDetails;
import fr.frogdevelopment.nihongo.entries.implementation.search.entity.SearchResult;
import fr.frogdevelopment.nihongo.entries.implementation.search.utils.Input;
import java.util.List;
import java.util.Set;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SearchDao {

    private static final String SQL_SEARCH_BY_KANJI =
            "SELECT pgroonga_highlight_html(e.kanji, pgroonga_query_extract_keywords(:query)) AS kanji,"
            + " e.kana,"
            + " s.sense_seq,"
            + " STRING_AGG(g.vocabulary, ', ') AS vocabulary"
            + " FROM entries e"
            + " INNER JOIN senses s ON e.entry_seq = s.entry_seq"
            + " INNER JOIN glosses g ON g.sense_seq = s.sense_seq AND g.lang = :lang"
            + " WHERE kanji @@ :query"
            + " GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq;";

    private static final String SQL_SEARCH_BY_KANA =
            "SELECT e.kanji,"
            + " pgroonga_highlight_html(e.kana, pgroonga_query_extract_keywords(:query)) AS kana,"
            + " s.sense_seq,"
            + " STRING_AGG(g.vocabulary, ', ') AS vocabulary"
            + " FROM entries e"
            + " INNER JOIN senses s ON e.entry_seq = s.entry_seq"
            + " INNER JOIN glosses g ON g.sense_seq = s.sense_seq AND g.lang = :lang"
            + " WHERE kana @@ :query"
            + " GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq;";

    private static final String SQL_SEARCH_BY_ROMAJI =
            "SELECT e.kanji,"
            + " e.kana,"
            + " s.sense_seq,"
            + " STRING_AGG(pgroonga_highlight_html(g.vocabulary, pgroonga_query_extract_keywords(:query)), ', ') AS vocabulary"
            + " FROM entries e"
            + " INNER JOIN senses s ON e.entry_seq = s.entry_seq AND s.sense_seq IN ("
            + "       SELECT g.sense_seq"
            + "       FROM glosses g"
            + "       WHERE g.lang = :lang"
            + "       AND vocabulary @@ :query)"
            + " INNER JOIN glosses g ON g.sense_seq = s.sense_seq AND g.lang = :lang"
            + " GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq;";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SearchDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional(propagation = REQUIRED)
    public List<SearchResult> search(String lang, String query, Input input) {
        String sql;
        switch (input) {
            case KANJI:
                sql = SQL_SEARCH_BY_KANJI;
                break;
            case KANA:
                sql = SQL_SEARCH_BY_KANA;
                break;
            case ROMAJI:
            default:
                sql = SQL_SEARCH_BY_ROMAJI;
        }

        var params = new MapSqlParameterSource();
        params.addValue("lang", lang);
        params.addValue("query", query);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> SearchResult.builder()
                .kanji(rs.getString("kanji"))
                .kana(rs.getString("kana"))
                .senseSeq(rs.getString("sense_seq"))
                .vocabulary(rs.getString("vocabulary"))
                .build());
    }

    @Transactional(propagation = REQUIRED)
    public SearchDetails getDetails(String lang, String senseSeq) {
        var sql =
                "SELECT e.entry_seq,"
                + " e.kanji,"
                + " e.kana,"
                + " e.reading,"
                + " s.pos,"
                + " s.field,"
                + " s.misc,"
                + " s.info,"
                + " s.dial,"
                + " STRING_AGG(g.vocabulary, ', ') AS vocabulary"
                + " FROM entries e"
                + " INNER JOIN senses s ON e.entry_seq = s.entry_seq AND s.sense_seq = :senseSeq"
                + " INNER JOIN glosses g ON g.sense_seq = s.sense_seq AND g.lang = :lang"
                + " GROUP BY e.entry_seq, e.kanji, e.reading, s.pos, s.field, s.misc, s.info, s.dial;";

        var paramSource = new MapSqlParameterSource();
        paramSource.addValue("senseSeq", senseSeq);
        paramSource.addValue("lang", lang);

        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, (rs, rowNum) -> SearchDetails.builder()
                .entrySeq(rs.getLong("entry_seq"))
                .kanji(rs.getString("kanji"))
                .kana(rs.getString("kana"))
                .reading(rs.getString("reading"))
                .pos(toSet(rs.getString("pos")))
                .field(toSet(rs.getString("field")))
                .misc(toSet(rs.getString("misc")))
                .info(rs.getString("info"))
                .dial(toSet(rs.getString("dial")))
                .gloss(rs.getString("vocabulary"))
                .build());
    }

    private static Set<String> toSet(String value) {
        return value == null ? Set.of() : Set.of(value.split(";"));
    }
}
