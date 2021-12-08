package com.frogdevelopment.nihongo.lessons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfiguration {

    @Bean
    SimpleJdbcInsert translationJdbcInsert(final DataSource dataSource) {
        return new SimpleJdbcInsert(dataSource)
                .withTableName("translations")
                .usingGeneratedKeyColumns("translation_id");
    }

    @Bean
    SimpleJdbcInsert japaneseJdbcInsert(final DataSource dataSource) {
        return new SimpleJdbcInsert(dataSource)
                .withTableName("japaneses")
                .usingGeneratedKeyColumns("japanese_id");
    }
}
