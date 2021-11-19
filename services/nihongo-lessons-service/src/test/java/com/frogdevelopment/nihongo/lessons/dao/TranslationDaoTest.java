package com.frogdevelopment.nihongo.lessons.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.entity.Translation;

@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
@Transactional(propagation = Propagation.REQUIRED)
class TranslationDaoTest {

    @Autowired
    private TranslationDao translationDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    void create() {
        // given
        var translation = Translation.builder()
                .japaneseId(1)
                .locale("LOCALE")
                .lesson(1)
                .input("INPUT")
                .sortLetter('I')
                .details("DETAILS")
                .example("EXAMPLE")
                .tags(List.of("TAG_1", "TAG_2", "TAG_3"))
                .build();

        // when
        var translationId = translationDao.create(1, translation);

        // then
        var map = jdbcTemplate.getJdbcTemplate()
                .queryForMap("SELECT * FROM translations WHERE translation_id=" + translationId);
        assertThat(map)
                .containsEntry("japanese_id", 1)
                .containsEntry("lesson", 1)
                .containsEntry("locale", "LOCALE")
                .containsEntry("input", "INPUT")
                .containsEntry("sort_letter", "I")
                .containsEntry("details", "DETAILS")
                .containsEntry("example", "EXAMPLE");
        var tags = (PgArray) map.get("tags");
        assertThat(tags).hasToString("{TAG_1,TAG_2,TAG_3}");
    }

    @Test
    void update() {
        // given
        var map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM translations WHERE translation_id = 1");
        assertThat(map)
                .containsEntry("lesson", 1)
                .containsEntry("locale", "en_US")
                .containsEntry("input", "I")
                .containsEntry("sort_letter", "I")
                .containsEntry("details", null)
                .containsEntry("example", null);
        var tags = (PgArray) map.get("tags");
        assertThat(tags).hasToString("{\"lesson 01\",\"lesson A\"}");

        var translation = Translation.builder()
                .id(1)
                .japaneseId(1)
                .lesson(2)
                .locale("LOCALE")
                .input("New INPUT_v2")
                .sortLetter('L')
                .details("DETAILS_v2")
                .example("EXAMPLE_v2")
                .tag("TAG_v2")
                .build();

        // when
        translationDao.update(translation);

        // then
        map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM translations WHERE translation_id = 1");
        assertThat(map)
                .containsEntry("lesson", 2)
                .containsEntry("locale", "LOCALE")
                .containsEntry("input", "New INPUT_v2")
                .containsEntry("sort_letter", "N")
                .containsEntry("details", "DETAILS_v2")
                .containsEntry("example", "EXAMPLE_v2");
        tags = (PgArray) map.get("tags");
        assertThat(tags).hasToString("{TAG_v2}");
    }

    @Test
    void delete() {
        // given
        var rowsInTable = countRowsInTableWhere(jdbcTemplate.getJdbcTemplate(), "translations", "translation_id = 1");
        assertThat(rowsInTable).isEqualTo(1);

        // when
        translationDao.delete(1);

        // then
        rowsInTable = countRowsInTableWhere(jdbcTemplate.getJdbcTemplate(), "translations", "translation_id = 1");
        assertThat(rowsInTable).isZero();
    }

}
