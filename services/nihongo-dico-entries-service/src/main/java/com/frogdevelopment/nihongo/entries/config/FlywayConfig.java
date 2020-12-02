package com.frogdevelopment.nihongo.entries.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationInitializer flywayInitializerPublic(DataSourceProperties dataSourceProperties) {
        return new FlywayMigrationInitializer(Flyway.configure()
                .schemas("public")
                .locations("classpath:db/migration/public")
                .dataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())
                .load());
    }

    @Bean
    public FlywayMigrationInitializer flywayInitializerJpn(DataSourceProperties dataSourceProperties) {
        return new FlywayMigrationInitializer(Flyway.configure()
                .schemas("jpn")
                .locations("classpath:db/migration/jpn")
                .dataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())
                .load());
    }

    @Bean
    public MultiSchemaFlywayMigrationInitializer flywayInitializerOther(DataSourceProperties dataSourceProperties) {
        return new MultiSchemaFlywayMigrationInitializer(
                Flyway.configure()
                        .locations("classpath:db/migration/other")
                        .dataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword()),
                Set.of("eng", "dut", "fre", "ger", "hun", "rus", "spa", "swe", "slv"));
    }
}
