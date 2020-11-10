package fr.frogdevelopment.nihongo.entries.implementation.export;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
class ExportDaoTest {

    @Value("classpath:sql/exportdao_languages.sql")
    private Resource data;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ExportDao exportDao;

    @Test
    void shouldRetrieveDistinctLanguages() {
        // given
        new ResourceDatabasePopulator(data).execute(dataSource);

        // when
        var languages = exportDao.getLanguages();

        // then
        assertThat(languages).containsExactlyInAnyOrder("fra", "eng", "dut", "rus", "slv");
    }
}
