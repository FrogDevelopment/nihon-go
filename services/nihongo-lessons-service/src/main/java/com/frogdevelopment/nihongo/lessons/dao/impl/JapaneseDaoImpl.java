package com.frogdevelopment.nihongo.lessons.dao.impl;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
@RequiredArgsConstructor
public class JapaneseDaoImpl implements JapaneseDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final SimpleJdbcInsert japaneseJdbcInsert;

    @Override
    public int create(final Japanese japanese) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("kanji", trimToNull(japanese.getKanji()));
        parameterSource.addValue("kana", trim(japanese.getKana()));
        parameterSource.addValue("lesson", japanese.getLesson());

        return japaneseJdbcInsert.executeAndReturnKey(parameterSource).intValue();
    }

    @Override
    public void update(final Japanese japanese) {
        final var sql = """
                UPDATE japaneses
                SET kanji = :kanji,
                    kana = :kana,
                    lesson = :lesson
                WHERE japanese_id = :japanese_id;""";

        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("japanese_id", japanese.getId());
        parameterSource.addValue("kanji", trimToNull(japanese.getKanji()));
        parameterSource.addValue("kana", trim(japanese.getKana()));
        parameterSource.addValue("lesson", japanese.getLesson());

        namedParameterJdbcOperations.update(sql, parameterSource);
    }

    @Override
    public void delete(final Japanese japanese) {
        final var sql = "DELETE FROM japaneses WHERE japanese_id = :japanese_id;";

        final var parameterSource = new MapSqlParameterSource("japanese_id", japanese.getId());

        namedParameterJdbcOperations.update(sql, parameterSource);
    }
}
