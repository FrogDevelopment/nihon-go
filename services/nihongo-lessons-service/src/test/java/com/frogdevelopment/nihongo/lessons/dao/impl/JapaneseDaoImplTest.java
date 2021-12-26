package com.frogdevelopment.nihongo.lessons.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.entity.Japanese;

@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
@Transactional(propagation = Propagation.REQUIRED)
class JapaneseDaoImplTest {

    @Autowired
    private JapaneseDaoImpl japaneseDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    void create() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .lesson(1)
                .build();

        // when
        var id = japaneseDao.create(japanese);

        // then
        var map = jdbcTemplate.getJdbcTemplate()
                .queryForMap("SELECT * FROM japaneses WHERE japanese_id=" + id);
        assertThat(map)
                .isNotNull()
                .hasSize(4)
                .containsEntry("kanji", "KANJI")
                .containsEntry("kana", "KANA")
                .containsEntry("lesson", 1);
    }

    @Test
    void update() {
        // given
        var map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM japaneses WHERE japanese_id = 1");
        assertThat(map)
                .isNotNull()
                .containsEntry("kanji", "私")
                .containsEntry("kana", "わたし");

        var japanese = Japanese.builder()
                .id(1)
                .kanji("KANJI")
                .kana("KANA")
                .lesson(1)
                .build();

        // when
        japaneseDao.update(japanese);

        // then
        map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM japaneses WHERE japanese_id = 1");
        assertThat(map)
                .isNotNull()
                .containsEntry("kanji", "KANJI")
                .containsEntry("kana", "KANA");
    }

    @Test
    void delete() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .lesson(1)
                .build();

        // when
        japaneseDao.create(japanese);
        japaneseDao.delete(japanese);

        // then
        var rowsInTable = countRowsInTableWhere(jdbcTemplate.getJdbcTemplate(), "japaneses",
                "japanese_id = " + japanese.getId());
        assertThat(rowsInTable).isZero();
    }

}
