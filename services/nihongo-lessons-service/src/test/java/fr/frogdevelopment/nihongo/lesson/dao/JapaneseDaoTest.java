package fr.frogdevelopment.nihongo.lesson.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import fr.frogdevelopment.nihongo.lesson.entity.Japanese;
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

@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
@Transactional(propagation = Propagation.REQUIRED)
class JapaneseDaoTest {

    @Autowired
    private JapaneseDao japaneseDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    void create() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .build();

        // when
        japaneseDao.create(japanese);

        // then
        var map = jdbcTemplate.getJdbcTemplate()
                .queryForMap("SELECT * FROM japaneses WHERE japanese_id=" + japanese.getId());
        assertThat(map).isNotNull();
        assertThat(map).hasSize(3);
        assertThat(map.get("kanji")).isEqualTo("KANJI");
        assertThat(map.get("kana")).isEqualTo("KANA");
    }

    @Test
    void update() {
        // given
        var map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM japaneses WHERE japanese_id = 1");
        assertThat(map).isNotNull();
        assertThat(map.get("kanji")).isNotEqualTo("KANJI");
        assertThat(map.get("kana")).isNotEqualTo("KANA");

        var japanese = Japanese.builder()
                .id(1)
                .kanji("KANJI")
                .kana("KANA")
                .build();

        // when
        japaneseDao.update(japanese);

        // then
        map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM japaneses WHERE japanese_id = 1");
        assertThat(map).isNotNull();
        assertThat(map.get("kanji")).isEqualTo("KANJI");
        assertThat(map.get("kana")).isEqualTo("KANA");
    }

    @Test
    void delete() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .build();

        // when
        japaneseDao.create(japanese);
        japaneseDao.delete(japanese);

        // then
        var rowsInTable = countRowsInTableWhere(jdbcTemplate.getJdbcTemplate(), "japaneses",
                "japanese_id = " + japanese.getId());
        assertThat(rowsInTable).isEqualTo(0);
    }

}
