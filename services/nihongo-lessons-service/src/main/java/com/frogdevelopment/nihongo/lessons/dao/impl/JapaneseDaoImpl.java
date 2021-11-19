package com.frogdevelopment.nihongo.lessons.dao.impl;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;

@Repository
@Transactional(propagation = MANDATORY)
public class JapaneseDaoImpl implements JapaneseDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JapaneseDaoImpl(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("japaneses")
                .usingGeneratedKeyColumns("japanese_id");
    }

    @Override
    public int create(final Japanese japanese) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("kanji", trimToNull(japanese.getKanji()));
        parameterSource.addValue("kana", trim(japanese.getKana()));

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).intValue();
    }

    @Override
    public void update(final Japanese japanese) {
        final var sql = "UPDATE japaneses SET kanji = :kanji, kana = :kana WHERE japanese_id = :japanese_id;";

        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("japanese_id", japanese.getId());
        parameterSource.addValue("kanji", trimToNull(japanese.getKanji()));
        parameterSource.addValue("kana", trim(japanese.getKana()));

        jdbcTemplate.update(sql, parameterSource);
    }


    @Override
    public void delete(final Japanese japanese) {
        final var sql = "DELETE FROM japaneses WHERE japanese_id = :japanese_id;";

        final var parameterSource = new MapSqlParameterSource("japanese_id", japanese.getId());

        jdbcTemplate.update(sql, parameterSource);
    }
}
