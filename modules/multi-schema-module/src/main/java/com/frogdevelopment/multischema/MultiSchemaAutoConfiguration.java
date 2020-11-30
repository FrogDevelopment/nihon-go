package com.frogdevelopment.multischema;

import com.frogdevelopment.multischema.database.MultiSchemaDataSourcesProperties;
import com.frogdevelopment.multischema.database.SchemaRoutingDataSource;
import com.frogdevelopment.multischema.database.SchemasManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(MultiSchemaDataSourcesProperties.class)
public class MultiSchemaAutoConfiguration {

    @Bean
    SchemasManager schemasManager(@Value("${spring.application.name}") String applicationName,
                                  DataSourceProperties dataSourceProperties,
                                  MultiSchemaDataSourcesProperties multiSchemaDataSourcesProperties) {
        return new SchemasManager(applicationName, dataSourceProperties, multiSchemaDataSourcesProperties);
    }

    @Bean
    SchemaRoutingDataSource schemaRoutingDataSource(SchemasManager schemasManager) {
        return new SchemaRoutingDataSource(schemasManager);
    }

}
