package com.frogdevelopment.nihongo.entries.implementation.about;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Map;
import javax.transaction.Transactional;
import org.postgresql.util.PGobject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;

import static javax.transaction.Transactional.TxType.REQUIRED;

@JdbcRepository(dialect = Dialect.POSTGRES)
@RequiredArgsConstructor
public class AboutDao {

    private final JdbcOperations jdbcOperations;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(REQUIRED)
    public void insert(final String date, final Map<String, Object> data) {
        final var sql = "INSERT INTO about(jmdict_date, nb_entries, languages) VALUES (?, ?, ?);";

        jdbcOperations.prepareStatement(sql, statement -> {
            statement.setString(1, date);
            statement.setInt(2, Integer.parseInt(data.get("nb_entries").toString()));
            statement.setObject(3, toPGobject(data));
            return statement.executeUpdate();
        });
    }

    private PGobject toPGobject(Map<String, Object> data) {
        try {
            final var jsonbObj = new PGobject();
            jsonbObj.setType("json");
            jsonbObj.setValue(objectMapper.writeValueAsString(data.get("languages")));
            return jsonbObj;
        } catch (final SQLException | JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Transactional(REQUIRED)
    public String getLast() {
        final var sql = """
                SELECT json_build_object('jmdict_date', jmdict_date, 'nb_entries', nb_entries, 'languages', languages)
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
