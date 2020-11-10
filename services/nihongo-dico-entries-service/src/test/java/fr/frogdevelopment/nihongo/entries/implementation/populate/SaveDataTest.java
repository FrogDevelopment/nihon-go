package fr.frogdevelopment.nihongo.entries.implementation.populate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class SaveDataTest {

    @InjectMocks
    private SaveData saveData;

    @Mock
    private DataSource dataSource;
    @Mock
    private ClearData clearData;
    @Mock
    private InsertData insertData;
    @Mock
    private UpdateVectorsIndex updateVectorsIndex;
    @Mock
    private Connection connection;

    @BeforeEach
    void setup() throws SQLException {
        given(dataSource
                .getConnection())
                .willReturn(connection);
    }

    @Test
    void shouldStopWhenClearDataThrowsException() throws SQLException, IOException {
        // given
        doThrow(SQLException.class)
                .when(clearData)
                .call(connection);

        // when
        final Throwable caughtThrowable = catchThrowable(() -> saveData.call());

        // then
        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(SQLException.class);
        var inOrder = Mockito.inOrder(clearData, insertData, updateVectorsIndex, connection);
        inOrder.verify(clearData).call(connection);
        inOrder.verify(insertData, never()).call(connection);
        inOrder.verify(updateVectorsIndex, never()).call(connection);
        inOrder.verify(connection).close();

    }

    @Test
    void shouldStopWhenSaveDataThrowsException() throws SQLException, IOException {
        // given
        doThrow(SQLException.class)
                .when(insertData)
                .call(connection);

        // when
        final Throwable caughtThrowable = catchThrowable(() -> saveData.call());

        // then
        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(SQLException.class);
        var inOrder = Mockito.inOrder(clearData, insertData, updateVectorsIndex, connection);
        inOrder.verify(clearData).call(connection);
        inOrder.verify(insertData).call(connection);
        inOrder.verify(updateVectorsIndex, never()).call(connection);
        inOrder.verify(connection).close();
    }

    @Test
    void shouldClearDataThenSaveDataThenUpdateIndexes() throws SQLException, IOException {
        // when
        saveData.call();

        // then
        var inOrder = Mockito.inOrder(clearData, insertData, updateVectorsIndex, connection);
        inOrder.verify(clearData).call(connection);
        inOrder.verify(insertData).call(connection);
        inOrder.verify(updateVectorsIndex).call(connection);
        inOrder.verify(connection).close();
    }
}
