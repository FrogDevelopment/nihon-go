package com.frogdevelopment.nihongo.lesson.dao;

import com.frogdevelopment.nihongo.lesson.entity.Japanese;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public class JapaneseDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JapaneseDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("japaneses")
                .usingGeneratedKeyColumns("japanese_id");
    }

    public void create(Japanese japanese) {
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("kanji", trimToNull(japanese.getKanji()));
        parameterSource.addValue("kana", trim(japanese.getKana()));

        var japaneseId = simpleJdbcInsert.executeAndReturnKey(parameterSource);

        japanese.setId(japaneseId.intValue());
    }

    public void update(Japanese japanese) {
        var sql = "UPDATE japaneses SET kanji = :kanji, kana = :kana WHERE japanese_id = :japanese_id;";

        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("japanese_id", japanese.getId());
        parameterSource.addValue("kanji", trimToNull(japanese.getKanji()));
        parameterSource.addValue("kana", trim(japanese.getKana()));

        jdbcTemplate.update(sql, parameterSource);
    }


    public void delete(Japanese japanese) {
        var sql = "DELETE FROM japaneses WHERE japanese_id = :japanese_id;";

        var parameterSource = new MapSqlParameterSource("japanese_id", japanese.getId());

        jdbcTemplate.update(sql, parameterSource);
    }
}
