package com.frogdevelopment.multischema.database;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Rollback
@Transactional
@SpringBootTest
@Tag("integrationTest")
class SchemasManagerTest {

    private static final String SCHEMA = "abc";

    @Autowired
    private SchemasManager schemasManager;
    @Autowired
    private JdbcOperations jdbcOperations;

    @Test
    void shouldCreateNewSchema() {
        // given
        assertThat(getSchemas().contains(SCHEMA)).isFalse();

        // when
        schemasManager.addSchema(SCHEMA);

        // then
        assertThat(getSchemas().contains(SCHEMA)).isTrue();

        var flywaySchemaHistory = jdbcOperations.queryForList("SELECT * from " + SCHEMA + ".flyway_schema_history");
        assertThat(flywaySchemaHistory).hasSize(2);
        assertSoftly(softAssertions -> {
            var history = flywaySchemaHistory.get(0);
            softAssertions.assertThat(history.get("installed_rank")).isEqualTo(0);
            softAssertions.assertThat(history.get("description")).isEqualTo("<< Flyway Schema Creation >>");
            softAssertions.assertThat(history.get("type")).isEqualTo("SCHEMA");
            softAssertions.assertThat(history.get("script")).isEqualTo(String.format("\"%s\"", SCHEMA));
        });
    }

    @Test
    void shouldDeleteSchema() {
        // given
        schemasManager.addSchema(SCHEMA);
        assertThat(getSchemas().contains(SCHEMA)).isTrue();

        // when
        schemasManager.removeSchema("abc");

        // then
        assertThat(getSchemas().contains(SCHEMA)).isFalse();
    }

    private List<String> getSchemas() {
        return jdbcOperations.queryForList("SELECT schema_name FROM information_schema.schemata;", String.class);
    }


}