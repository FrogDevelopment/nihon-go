package com.frogdevelopment.nihongo.lessons.dao.impl;

import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.Translation;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.frogdevelopment.nihongo.lessons.Utils.getSortLetter;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
@RequiredArgsConstructor
public class TranslationDaoImpl implements TranslationDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final SimpleJdbcInsert translationJdbcInsert;

    @Override
    public int create(final int japaneseId, final Translation translation) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("japanese_id", japaneseId);
        parameterSource.addValue("locale", translation.getLocale());
        parameterSource.addValue("input", trim(translation.getInput()));
        parameterSource.addValue("sort_letter", getSortLetter(translation.getInput()));
        parameterSource.addValue("details", trimToNull(translation.getDetails()));
        parameterSource.addValue("example", trimToNull(translation.getExample()));

        return translationJdbcInsert.executeAndReturnKey(parameterSource).intValue();
    }

    @Override
    public void deleteJapaneseTranslations(final int japaneseId) {
        final var sql = "DELETE FROM translations WHERE japanese_id = :japanese_id";

        final var parameterSource = new MapSqlParameterSource("japanese_id", japaneseId);

        namedParameterJdbcOperations.update(sql, parameterSource);
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

        namedParameterJdbcOperations.update(sql, parameterSource);
    }
}
