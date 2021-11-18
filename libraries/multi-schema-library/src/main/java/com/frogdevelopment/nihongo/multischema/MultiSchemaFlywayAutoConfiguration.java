package com.frogdevelopment.nihongo.multischema;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Flyway.class)
@ConditionalOnProperty(name = "spring.flyway.multi-schema", havingValue = "true")
@AutoConfigureAfter({
        DataSourceAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class MultiSchemaFlywayAutoConfiguration {

    @Bean
    @Order(1)
    public FlywayMigrationInitializer flywayInitializerPublic(DataSource dataSource) {
        return new FlywayMigrationInitializer(Flyway.configure()
                .schemas("public")
                .locations("classpath:db/migration/public")
                .dataSource(dataSource)
                .load());
    }

    @Bean
    @Order(2)
    public FlywayMigrationInitializer flywayInitializerJpn(DataSource dataSource) {
        return new FlywayMigrationInitializer(Flyway.configure()
                .schemas("jpn")
                .locations("classpath:db/migration/jpn")
                .dataSource(dataSource)
                .load());
    }

    @Bean
    @Order(3)
    public MultiSchemaFlywayMigrationInitializer flywayInitializerOther(DataSource dataSource) {
        return new MultiSchemaFlywayMigrationInitializer(Flyway.configure()
                .locations("classpath:db/migration/other")
                .dataSource(dataSource));
    }
}
