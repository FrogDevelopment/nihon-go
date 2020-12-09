package fr.frogdevelopment.authentication.application.user;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class GetUserDetails {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GetUserDetails(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserDto call(String username) {
        var sql = """
                SELECT u.username,
                      u.enabled,
                      ARRAY_AGG(a.authority) AS authorities,
                      ARRAY_AGG(ac.action) AS actions
                FROM users u
                        INNER JOIN authorities a ON u.username = a.username
                        INNER JOIN actions ac ON u.username = ac.username
                WHERE u.username = :username
                GROUP BY u.username, u.enabled
                """;

        var params = new MapSqlParameterSource("username", username);

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> UserDto.builder()
                .username(rs.getString("username"))
                .enabled(rs.getBoolean("enabled"))
                .authorities(getArray(rs, "authorities"))
                .actions(getArray(rs, "actions"))
                .build());
    }

    private static List<String> getArray(ResultSet rs, String columnLabel) throws SQLException {
        return Arrays.asList((String[]) rs.getArray(columnLabel).getArray());
    }
}
