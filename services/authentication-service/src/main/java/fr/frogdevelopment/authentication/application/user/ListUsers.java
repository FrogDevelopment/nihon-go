package fr.frogdevelopment.authentication.application.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Repository
public class ListUsers {

    private final JdbcTemplate jdbcTemplate;

    public ListUsers(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserDto> call() {
        var sql = """
                SELECT u.username,
                       u.enabled,
                       ARRAY_AGG(a.authority) AS authorities
                FROM users u
                        INNER JOIN authorities a ON u.username = a.username
                GROUP BY u.username, u.enabled
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> UserDto.builder()
                .username(rs.getString("username"))
                .enabled(rs.getBoolean("enabled"))
                .authorities(Arrays.asList((String[]) rs.getArray("authorities").getArray()))
                .build());
    }
}
