package com.frogdevelopment.multischema;

import com.frogdevelopment.multischema.database.MultiSchemaDataSourcesProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MultiSchemaDataSourcesProperties.class)
public class TestApplication {

}

