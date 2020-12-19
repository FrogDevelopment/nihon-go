package com.frogdevelopment.nihongo.sentences.implementation.populate;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
class ClearImportTable {

    void call(Connection connection, String tableName) throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE " + tableName + ";");
        }
    }

}
