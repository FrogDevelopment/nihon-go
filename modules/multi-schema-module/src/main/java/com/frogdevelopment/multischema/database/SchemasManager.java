package com.frogdevelopment.multischema.database;

import com.frogdevelopment.multischema.database.exception.UnknownSchemaDataSourceException;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.database.DatabaseFactory;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SchemasManager implements InitializingBean {

    private static final String SQL_LIST_REALM_SCHEMAS = "SELECT schema_name"
            + " FROM information_schema.schemata"
            + " WHERE schema_name != 'information_schema'"
            + " AND schema_name NOT LIKE 'pg_%';";

    private final String applicationName;
    private final DataSourceProperties dataSourceProperties;
    private final MultiSchemaDataSourcesProperties multiSchemaDataSourcesProperties;
    private final Map<String, DataSource> dataSources = new HashMap<>();
    private final DataSourceBuilder<HikariDataSource> dataSourceBuilder;

    private String jdbcUrl;

    public SchemasManager(String applicationName,
                          DataSourceProperties dataSourceProperties,
                          MultiSchemaDataSourcesProperties multiSchemaDataSourcesProperties) {
        this.applicationName = applicationName;
        this.dataSourceProperties = dataSourceProperties;
        this.dataSourceBuilder = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class);
        this.multiSchemaDataSourcesProperties = multiSchemaDataSourcesProperties;
    }

    public void afterPropertiesSet() {
        var url = dataSourceProperties.getUrl();
        jdbcUrl = url.split("\\?", 2)[0] + "?ApplicationName=" + applicationName;

        var username = dataSourceProperties.getUsername();
        var password = dataSourceProperties.getPassword();
        try (var connection = DriverManager.getConnection(url, username, password);
             var rs = connection.createStatement().executeQuery(SQL_LIST_REALM_SCHEMAS)) {
            while (rs.next()) {
                addSchema(rs.getString("schema_name"));
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void addSchema(String schema) {
        dataSources.computeIfAbsent(schema.toLowerCase(), this::migrateSchema);
    }

    private HikariDataSource migrateSchema(String schema) {
        var dataSource = dataSourceBuilder.url(jdbcUrl).build();
        dataSource.setPoolName("schema#" + schema);
        dataSource.setSchema(schema);
        dataSource.setConnectionTestQuery(multiSchemaDataSourcesProperties.getConnectionTestQuery());
        dataSource.setMaximumPoolSize(multiSchemaDataSourcesProperties.getMaximumPoolSize());
        dataSource.setMinimumIdle(multiSchemaDataSourcesProperties.getMinimumIdle());
        dataSource.setIdleTimeout(multiSchemaDataSourcesProperties.getIdleTimeout().toMillis());

        log.info("Migrating schema {}", schema);
        Flyway.configure()
                .schemas(schema)
                .dataSource(dataSource)
                .load()
                .migrate();
        return dataSource;
    }

    public void removeSchema(String schemaName) {
        this.dataSources.computeIfPresent(schemaName.toLowerCase(), this::dropSchema);
    }

    private DataSource dropSchema(String schemaName, DataSource dataSource) {
        var configuration = Flyway.configure()
                .schemas(schemaName)
                .dataSource(dataSource);
        var jdbcConnectionFactory = new JdbcConnectionFactory(configuration.getDataSource(), configuration.getConnectRetries());
        var database = DatabaseFactory.createDatabase(configuration, true, jdbcConnectionFactory);
        var schema = database.getMainConnection().getSchema(schemaName);

        log.info("Dropping schema {} ...", schema);
        schema.drop();

        return null;
    }

    DataSource getDataSource(final String schema) {
        if (schema == null) {
            log.warn("No schema set on context, using default \"{}\"", multiSchemaDataSourcesProperties.getDefaultSchema());
            return getDataSource(multiSchemaDataSourcesProperties.getDefaultSchema());
        } else {
            log.debug("Querying schema \"{}\"", schema);
            return Optional
                    .ofNullable(this.dataSources.get(schema.toLowerCase()))
                    .orElseThrow(() -> new UnknownSchemaDataSourceException(schema));
        }
    }

}
