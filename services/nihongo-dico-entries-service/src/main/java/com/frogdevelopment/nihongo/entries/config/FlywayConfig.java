package com.frogdevelopment.nihongo.entries.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationInitializer flywayInitializerPublic(DataSource dataSource) {
        return new FlywayMigrationInitializer(Flyway.configure()
                .schemas("public")
                .locations("classpath:db/migration/public")
                .dataSource(dataSource)
                .load());
    }

    @Bean
    public FlywayMigrationInitializer flywayInitializerJpn(DataSource dataSource) {
        return new FlywayMigrationInitializer(Flyway.configure()
                .schemas("jpn")
                .locations("classpath:db/migration/jpn")
                .dataSource(dataSource)
                .load());
    }

    @Bean
    public MultiSchemaFlywayMigrationInitializer flywayInitializerOther(DataSource dataSource) {
        return new MultiSchemaFlywayMigrationInitializer(
                Flyway.configure()
                        .locations("classpath:db/migration/other")
                        .dataSource(dataSource));
    }
}
