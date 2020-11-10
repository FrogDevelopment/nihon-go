package fr.frogdevelopment.nihongo.entries.implementation.populate;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaveData {

    private final DataSource dataSource;
    private final ClearData clearData;
    private final InsertData insertData;
    private final UpdateVectorsIndex updateVectorsIndex;

    public SaveData(DataSource dataSource,
                    ClearData clearData,
                    InsertData insertData,
                    UpdateVectorsIndex updateVectorsIndex) {
        this.dataSource = dataSource;
        this.clearData = clearData;
        this.insertData = insertData;
        this.updateVectorsIndex = updateVectorsIndex;
    }

    public void call() {
        try (var connection = dataSource.getConnection()) {
            log.info("****** Clear database");
            clearData.call(connection);

            log.info("****** Insert data to database");
            insertData.call(connection);

            log.info("****** Updating the vectors");
            updateVectorsIndex.call(connection);
        } catch (IOException | SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
