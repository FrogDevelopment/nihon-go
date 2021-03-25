package com.frogdevelopment.nihongo.lessons.dao;

import com.frogdevelopment.nihongo.lessons.entity.Translation;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

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
        var translation = new Translation();
        translation.setJapaneseId(1);
        translation.setLocale("LOCALE");
        translation.setInput("INPUT");
        translation.setDetails("DETAILS");
        translation.setExample("EXAMPLE");
        List<String> tags = List.of("TAG_1", "TAG_2", "TAG_3");
        translation.setTags(tags);

        // when
        translationDao.create(translation);

        // then
        assertThat(translation.getId()).isNotNull();
        var map = jdbcTemplate.getJdbcTemplate()
                .queryForMap("SELECT * FROM translations WHERE translation_id=" + translation.getId());
        assertThat(map).isNotNull();
        assertThat(map).hasSize(8);
        assertThat(map.get("japanese_id")).isEqualTo(translation.getJapaneseId());
        assertThat(map.get("locale")).isEqualTo(translation.getLocale());
        assertThat(map.get("input")).isEqualTo(translation.getInput());
        assertThat(map.get("sort_letter")).isEqualTo(translation.getSortLetter());
        assertThat(map.get("details")).isEqualTo(translation.getDetails());
        assertThat(map.get("example")).isEqualTo(translation.getExample());
//       fixme assertThat(map.get("tags")).isEqualTo(translation.getTags());
//        Expecting:
// <{TAG_1,TAG_2,TAG_3}>
//        to be equal to:
// <["TAG_1", "TAG_2", "TAG_3"]>
//        but was not.
//        Expected :[TAG_1, TAG_2, TAG_3]
//        Actual   :{TAG_1,TAG_2,TAG_3}
    }

    @Test
    void update() {
        // given
        var map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM translations WHERE translation_id = 1");
        assertThat(map).isNotNull();
        assertThat(map).hasSize(8);
        assertThat(map.get("locale")).isNotEqualTo("LOCALE");
        assertThat(map.get("input")).isNotEqualTo("NEW INPUT");
        assertThat(map.get("sort_letter")).isNotEqualTo("N");
        assertThat(map.get("details")).isNotEqualTo("DETAILS");
        assertThat(map.get("example")).isNotEqualTo("EXAMPLE");
//        assertThat(list).extracting("locale").containsOnly("LOCALE", "LOCALE_2");

        var translation = Translation.builder()
                .id(1)
                .japaneseId(1)
                .locale("LOCALE")
                .input("NEW INPUT")
                .details("DETAILS")
                .example("EXAMPLE")
                .tag("TAG_1")
                .tag("TAG_2")
                .tag("TAG_3")
                .build();

        // when
        translationDao.update(translation);

        // then
        map = jdbcTemplate.getJdbcTemplate().queryForMap("SELECT * FROM translations WHERE translation_id = 1");
        assertThat(map).isNotNull();
        assertThat(map).hasSize(8);
        assertThat(map.get("locale")).isEqualTo(translation.getLocale());
        assertThat(map.get("input")).isEqualTo(translation.getInput());
        assertThat(map.get("sort_letter")).isEqualTo(translation.getSortLetter());
        assertThat(map.get("details")).isEqualTo(translation.getDetails());
        assertThat(map.get("example")).isEqualTo(translation.getExample());
//        assertThat(list).extracting("locale").containsOnly("LOCALE", "LOCALE_2");
    }

    @Test
    void delete() {
        // given
        var rowsInTable = countRowsInTableWhere(jdbcTemplate.getJdbcTemplate(), "translations", "translation_id = 1");
        assertThat(rowsInTable).isEqualTo(1);

        var translation = Translation.builder()
                .id(1)
                .build();

        // when
        translationDao.delete(translation);

        // then
        rowsInTable = countRowsInTableWhere(jdbcTemplate.getJdbcTemplate(), "translations", "translation_id = 1");
        assertThat(rowsInTable).isEqualTo(0);
    }

}
