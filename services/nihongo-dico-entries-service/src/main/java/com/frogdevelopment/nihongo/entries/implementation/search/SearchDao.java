package com.frogdevelopment.nihongo.entries.implementation.search;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.intellij.lang.annotations.Language;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchDetails;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchResult;
import com.frogdevelopment.nihongo.entries.implementation.search.utils.Input;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;

import static javax.transaction.Transactional.TxType.REQUIRED;

@JdbcRepository(dialect = Dialect.POSTGRES)
@RequiredArgsConstructor
public abstract class SearchDao {

    @Language("SQL")
    private static final String SQL_SEARCH_BY_KANJI = """
            SELECT pgroonga_highlight_html(e.kanji, pgroonga_query_extract_keywords(?)) AS kanji,
            e.kana,
            s.sense_seq,
            STRING_AGG(g.vocabulary, ', ') AS vocabulary
            FROM jpn.entries e
            INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq
            INNER JOIN %s.glosses g ON g.sense_seq = s.sense_seq
            WHERE kanji @@ ?
            GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq;
            """;

    @Language("SQL")
    private static final String SQL_SEARCH_BY_KANA = """
            SELECT e.kanji,
            pgroonga_highlight_html(e.kana, pgroonga_query_extract_keywords(?)) AS kana,
            s.sense_seq,
            STRING_AGG(g.vocabulary, ', ') AS vocabulary
            FROM jpn.entries e
            INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq
            INNER JOIN %s.glosses g ON g.sense_seq = s.sense_seq
            WHERE kana @@ ?
            GROUP BY e.entry_seq, e.kanji, e.reading, s.sense_seq
            """;

    @Language("SQL")
    private static final String SQL_SEARCH_BY_ROMAJI = """
            SELECT e.kanji,
            e.kana,
            s.sense_seq,
            STRING_AGG(pgroonga_highlight_html(g.vocabulary, pgroonga_query_extract_keywords(?)), ', ') AS vocabulary
            FROM jpn.entries e
            INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq AND s.sense_seq IN (
                 SELECT g.sense_seq
                 FROM %1$S.glosses g
                 WHERE vocabulary @@ ?)
            INNER JOIN %1$S.glosses g ON g.sense_seq = S.sense_seq
            GROUP BY e.entry_seq, e.kanji, e.reading, S.sense_seq;""";

    @Language("SQL")
    private static final String SQL_SEARCH_DETAILS = """
            SELECT e.entry_seq,
            e.kanji,
            e.kana,
            e.reading,
            s.pos,
            s.field,
            s.misc,
            s.info,
            s.dial,
            STRING_AGG(g.vocabulary, ', ') AS vocabulary
            FROM jpn.entries e
            INNER JOIN jpn.senses s ON e.entry_seq = s.entry_seq AND s.sense_seq = ?
            INNER JOIN %s.glosses g ON g.sense_seq = s.sense_seq
            GROUP BY e.entry_seq, e.kanji, e.reading, s.pos, s.field, s.misc, s.info, s.dial;
            """;

    private final JdbcOperations jdbcOperations;

    @Transactional(REQUIRED)
    public List<SearchResult> search(final String lang, final String query, final Input input) {
        final var sql = switch (input) {
            case KANJI -> SQL_SEARCH_BY_KANJI;
            case KANA -> SQL_SEARCH_BY_KANA;
            default -> SQL_SEARCH_BY_ROMAJI;
        };
        return jdbcOperations.prepareStatement(sql.formatted(lang), statement -> {
            statement.setString(1, query);
            statement.setString(2, query);

            final var rs = statement.executeQuery();

            List<SearchResult> results = new ArrayList<>();
            while (rs.next()) {
                results.add(SearchResult.builder()
                        .kanji(rs.getString("kanji"))
                        .kana(rs.getString("kana"))
                        .senseSeq(rs.getString("sense_seq"))
                        .vocabulary(rs.getString("vocabulary"))
                        .build());
            }

            return results;
        });
    }

    @Transactional(REQUIRED)
    public SearchDetails getDetails(final String lang, final String senseSeq) {
        final var sql = String.format(SQL_SEARCH_DETAILS, lang);

        return jdbcOperations.prepareStatement(sql, statement -> {
            statement.setString(1, senseSeq);

            final var rs = statement.executeQuery();
            if (rs.next()) {
                return  SearchDetails.builder()
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
                        .build();
            }

            return null;
        });
    }

    private static Set<String> toSet(final String value) {
        return value == null ? Set.of() : Set.of(value.split(";"));
    }
}

