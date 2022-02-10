package com.frogdevelopment.nihongo.sentences.implementation.about;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Map;

import static javax.transaction.Transactional.TxType.REQUIRED;

@Slf4j
@JdbcRepository(dialect = Dialect.POSTGRES)
@RequiredArgsConstructor
public class AboutDao {

    private final JdbcOperations jdbcOperations;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(REQUIRED)
    public void generate(final Map<String, Integer> data) {
        final var sql = "INSERT INTO about(date_import, languages) VALUES (NOW(), ?);";

        jdbcOperations.prepareStatement(sql, statement -> {
            statement.setObject(1, toPGobject(data));
            return statement.executeUpdate();
        });
    }

    private PGobject toPGobject(final Map<String, Integer> data) {
        try {
            final var jsonbObj = new PGobject();
            jsonbObj.setType("json");
            jsonbObj.setValue(objectMapper.writeValueAsString(data));
            return jsonbObj;
        } catch (final SQLException | JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Transactional(REQUIRED)
    public String getLast() {
        final String sql = """
                SELECT JSON_BUILD_OBJECT('date_import', date_import, 'languages', languages)
                FROM about
                ORDER BY about_id DESC
                LIMIT 1
                """;

        return jdbcOperations.prepareStatement(sql, statement -> {
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return "no data";
        });
    }

    @Transactional(REQUIRED)
    public String getLanguages() {
        final String sql = """
                SELECT languages
                FROM about
                ORDER BY about_id
                DESC LIMIT 1
                """;

        return jdbcOperations.prepareStatement(sql, statement -> {
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return "no data";
        });
    }

}
