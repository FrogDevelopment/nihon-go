package com.frogdevelopment.multischema.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties("multischema.datasources")
public class MultiSchemaDataSourcesProperties {

    private String defaultSchema = "public";
    private String connectionTestQuery = "SELECT 1;";
    private int maximumPoolSize = 5;
    private int minimumIdle = 1;
    private Duration idleTimeout = Duration.ofMinutes(5);
}
