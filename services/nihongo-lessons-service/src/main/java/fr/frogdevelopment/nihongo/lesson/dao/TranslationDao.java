package fr.frogdevelopment.nihongo.lesson.dao;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import fr.frogdevelopment.nihongo.lesson.entity.Translation;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = MANDATORY)
public class TranslationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public TranslationDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("translations")
                .usingGeneratedKeyColumns("translation_id");
    }

    public void create(Translation translation) {
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("japanese_id", translation.getJapaneseId());
        parameterSource.addValue("locale", translation.getLocale());
        parameterSource.addValue("input", trim(translation.getInput()));
        parameterSource.addValue("sort_letter", translation.computeSortLetter());
        parameterSource.addValue("details", trimToNull(translation.getDetails()));
        parameterSource.addValue("example", trimToNull(translation.getExample()));
        parameterSource.addValue("tags", translation.getTags().toArray(new String[0]));

        var translationId = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        translation.setId(translationId.intValue());
    }

    public void delete(Translation translation) {
        var sql = "DELETE FROM translations WHERE translation_id = :japanese_id";

        var parameterSource = new MapSqlParameterSource("japanese_id", translation.getId());

        jdbcTemplate.update(sql, parameterSource);
    }

    public void update(Translation translation) {
        var sql = "UPDATE translations SET locale = :locale, input = :input, sort_letter = :sort_letter, details = :details, example = :example WHERE translation_id = :translation_id";

        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("translation_id", translation.getId());
        parameterSource.addValue("locale", translation.getLocale());
        parameterSource.addValue("input", StringUtils.trim(translation.getInput()));
        parameterSource.addValue("sort_letter", translation.computeSortLetter());
        parameterSource.addValue("details", StringUtils.trimToNull(translation.getDetails()));
        parameterSource.addValue("example", StringUtils.trimToNull(translation.getExample()));

        jdbcTemplate.update(sql, parameterSource);
    }
}
