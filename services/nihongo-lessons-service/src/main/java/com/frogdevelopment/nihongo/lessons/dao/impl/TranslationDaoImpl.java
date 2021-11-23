package com.frogdevelopment.nihongo.lessons.dao.impl;

import static com.frogdevelopment.nihongo.lessons.Utils.getSortLetter;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

@Repository
@Transactional(propagation = MANDATORY)
public class TranslationDaoImpl implements TranslationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public TranslationDaoImpl(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("translations")
                .usingGeneratedKeyColumns("translation_id");
    }

    @Override
    public int create(final int japaneseId, final Translation translation) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("japanese_id", japaneseId);
        parameterSource.addValue("locale", translation.getLocale());
        parameterSource.addValue("input", trim(translation.getInput()));
        parameterSource.addValue("sort_letter", getSortLetter(translation.getInput()));
        parameterSource.addValue("details", trimToNull(translation.getDetails()));
        parameterSource.addValue("example", trimToNull(translation.getExample()));

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).intValue();
    }

    @Override
    public void delete(int translationId) {
        final var sql = "DELETE FROM translations WHERE translation_id = :japanese_id";

        final var parameterSource = new MapSqlParameterSource("japanese_id", translationId);

        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void update(final Translation translation) {
        final var sql = """
                UPDATE translations
                SET locale = :locale,
                    input = :input,
                    sort_letter = :sort_letter,
                    details = :details,
                    example = :example
                WHERE translation_id = :translation_id""";

        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("translation_id", translation.getId());
        parameterSource.addValue("locale", translation.getLocale());
        parameterSource.addValue("input", trim(translation.getInput()));
        parameterSource.addValue("sort_letter", getSortLetter(translation.getInput()));
        parameterSource.addValue("details", trimToNull(translation.getDetails()));
        parameterSource.addValue("example", trimToNull(translation.getExample()));

        jdbcTemplate.update(sql, parameterSource);
    }
}
