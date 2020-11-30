package com.frogdevelopment.multischema.database;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaRoutingDataSource extends AbstractDataSource {

    private final SchemasManager schemasManager;

    public SchemaRoutingDataSource(SchemasManager schemasManager) {
        this.schemasManager = schemasManager;
    }

    public DataSource getDataSource(String schema) {
        return schemasManager.getDataSource(schema);
    }

    private DataSource getCurrentDataSource() {
        return getDataSource(SchemaContextHolder.get());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getCurrentDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getCurrentDataSource().getConnection(username, password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInstance(this)) {
            return (T) this;
        }
        return getCurrentDataSource().unwrap(clazz);
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        return (clazz.isInstance(this) || getCurrentDataSource().isWrapperFor(clazz));
    }

}
