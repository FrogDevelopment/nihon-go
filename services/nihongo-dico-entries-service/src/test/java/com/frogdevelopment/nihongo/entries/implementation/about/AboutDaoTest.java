package com.frogdevelopment.nihongo.entries.implementation.about;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@Tag("integrationTest")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Testcontainers
class AboutDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AboutDao aboutDao;

    @Test
    void test_insert_getLast() throws JSONException {
        // given
        var date = "2018-09-20";
        jdbcTemplate.update("INSERT INTO entries(entry_seq, kanji, reading) VALUES ('123', NULL, 'XXX');");
        jdbcTemplate.update("INSERT INTO senses(sense_seq, entry_seq) VALUES ('123_1', '123');");
        jdbcTemplate
                .update("INSERT INTO glosses(sense_seq, lang, vocabulary) VALUES ('123_1', 'fre', 'test'), ('123_1', 'fre', 'test2'), ('123_1', 'eng', 'test3');");

        // when
        aboutDao.insert(date);

        // and
        var about = aboutDao.getLast();

        // then
        var aboutObject = new JSONObject(about);
        assertThat(aboutObject.getString("jmdict_date")).isEqualTo(date);
        assertThat(aboutObject.getInt("nb_entries")).isEqualTo(1);
        assertThat(aboutObject.has("languages")).isTrue();
        var languagesObject = new JSONObject(aboutObject.getString("languages"));
        assertThat(languagesObject.getInt("fre")).isEqualTo(2);
        assertThat(languagesObject.getInt("eng")).isEqualTo(1);
    }
}
