package fr.frogdevelopment.nihongo.sentence.implementation.populate;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
class ClearImportTable {

    void call(Connection connection, String tableName) throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE " + tableName);
        }
    }

}
