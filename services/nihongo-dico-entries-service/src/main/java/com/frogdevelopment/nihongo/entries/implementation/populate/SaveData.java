package com.frogdevelopment.nihongo.entries.implementation.populate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveData {

    private final DataSource dataSource;
    private final ClearData clearData;
    private final InsertData insertData;
    private final UpdateVectorsIndex updateVectorsIndex;

    public Map<String, Object> call() {
        try (final var connection = dataSource.getConnection()) {
            log.info("****** Clear database");
            clearData.call(connection);

            log.info("****** Insert data to database");
            final var map = insertData.call(connection);

            log.info("****** Updating the vectors");
            updateVectorsIndex.call(connection);

            return map;
        } catch (final IOException | SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
