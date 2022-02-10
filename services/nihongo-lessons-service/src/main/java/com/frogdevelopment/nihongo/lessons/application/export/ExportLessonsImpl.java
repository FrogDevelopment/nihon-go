package com.frogdevelopment.nihongo.lessons.application.export;

import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.transaction.Transactional;
import org.intellij.lang.annotations.Language;
import com.frogdevelopment.nihongo.export.DataFtpExporter;
import com.frogdevelopment.nihongo.export.DataFtpExporter.Export;
import com.frogdevelopment.nihongo.lessons.application.ExportLessons;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;

import io.micronaut.data.jdbc.runtime.JdbcOperations;
import jakarta.inject.Singleton;

import static javax.transaction.Transactional.TxType.REQUIRED;

@Singleton
@RequiredArgsConstructor
public class ExportLessonsImpl implements ExportLessons {

    @Language("SQL")
    private static final String SQL_LOCALES = """
            SELECT DISTINCT t.locale
            FROM lessons l
            INNER JOIN japaneses j ON l.lesson = j.lesson
            INNER JOIN translations t ON j.japanese_id = t.japanese_id
            WHERE l.lesson = ?
            """;

    @Language("SQL")
    private static final String SQL_COPY = """
            COPY (SELECT j.kanji,
                       j.kana,
                       t.sort_letter,
                       t.input,
                       t.details,
                       t.example
                 FROM japaneses j
                 INNER JOIN translations t
                            ON j.japanese_id = t.japanese_id
                            AND t.locale = '%1$s'
                            AND lesson = %2$S)
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final DataFtpExporter dataFtpExporter;
    private final JdbcOperations jdbcOperations;
    private final LessonDao lessonDao;

    @Override
    @Transactional(REQUIRED)
    public void call(final int lesson) {
        final var exports = jdbcOperations.prepareStatement(SQL_LOCALES, statement -> {
            statement.setInt(1, lesson);

            var result = new ArrayList<Export>();
            final var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(toExport(lesson, resultSet));
            }

            return result;
        });

        dataFtpExporter.export(exports);

        lessonDao.updateExportTime(lesson);
    }

    private Export toExport(final int lesson, final ResultSet rs) throws SQLException {
        final String locale = rs.getString("locale");
        final var fileName = "%s-%02d".formatted(locale, lesson);
        return new Export(fileName, SQL_COPY.formatted(locale, lesson));
    }
}
