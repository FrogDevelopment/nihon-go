package com.frogdevelopment.nihongo.lessons.dao;

import com.frogdevelopment.nihongo.lessons.entity.Translation;
import org.apache.commons.lang3.StringUtils;
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
public class TranslationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public TranslationDao(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("translations")
                .usingGeneratedKeyColumns("translation_id");
    }

    public void create(final Translation translation) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("japanese_id", translation.getJapaneseId());
        parameterSource.addValue("locale", translation.getLocale());
        parameterSource.addValue("input", trim(translation.getInput()));
        parameterSource.addValue("sort_letter", translation.computeSortLetter());
        parameterSource.addValue("details", trimToNull(translation.getDetails()));
        parameterSource.addValue("example", trimToNull(translation.getExample()));
        parameterSource.addValue("tags", translation.getTags().toArray(new String[0]));

        final var translationId = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        translation.setId(translationId.intValue());
    }

    public void delete(final Translation translation) {
        final var sql = "DELETE FROM translations WHERE translation_id = :japanese_id";

        final var parameterSource = new MapSqlParameterSource("japanese_id", translation.getId());

        jdbcTemplate.update(sql, parameterSource);
    }

    public void update(final Translation translation) {
        final var sql = "UPDATE translations SET locale = :locale, input = :input, sort_letter = :sort_letter, details = :details, example = :example WHERE translation_id = :translation_id";

        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("translation_id", translation.getId());
        parameterSource.addValue("locale", translation.getLocale());
        parameterSource.addValue("input", StringUtils.trim(translation.getInput()));
        parameterSource.addValue("sort_letter", translation.computeSortLetter());
        parameterSource.addValue("details", StringUtils.trimToNull(translation.getDetails()));
        parameterSource.addValue("example", StringUtils.trimToNull(translation.getExample()));

        jdbcTemplate.update(sql, parameterSource);
    }
}
