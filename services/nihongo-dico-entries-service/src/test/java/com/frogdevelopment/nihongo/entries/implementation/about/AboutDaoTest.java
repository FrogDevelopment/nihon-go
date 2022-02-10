package com.frogdevelopment.nihongo.entries.implementation.about;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integrationTest")
@MicronautTest(startApplication = false)
class AboutDaoTest {

    @Inject
    private AboutDao aboutDao;

    @Test
    void test_insert_getLast() throws JSONException {
        // given
        var date = "2018-09-20";
        var data = new HashMap<String, Object>();
        data.put("nb_entries", 123456L);
        var languageMap = new HashMap<String, Long>();
        languageMap.put("eng", 123L);
        languageMap.put("fra", 456L);
        languageMap.put("spa", 789L);
        data.put("languages", languageMap);

        // when
        aboutDao.insert(date, data);
        var about = aboutDao.getLast();

        // then
        var aboutObject = new JSONObject(about);
        assertThat(aboutObject.getString("jmdict_date")).isEqualTo(date);
        assertThat(aboutObject.getInt("nb_entries")).isEqualTo(123456);
        assertThat(aboutObject.has("languages")).isTrue();
        var languagesObject = aboutObject.getJSONObject("languages");
        assertThat(languagesObject.getInt("eng")).isEqualTo(123);
        assertThat(languagesObject.getInt("fra")).isEqualTo(456);
        assertThat(languagesObject.getInt("spa")).isEqualTo(789);
    }

    @Test
    void test_insert_getLanguages() throws JSONException {
        // given
        var date = "2018-09-20";
        var data = new HashMap<String, Object>();
        data.put("nb_entries", 123456L);
        var languageMap = new HashMap<String, Long>();
        languageMap.put("eng", 123L);
        languageMap.put("fra", 456L);
        languageMap.put("spa", 789L);
        data.put("languages", languageMap);

        // when
        aboutDao.insert(date, data);
        var languages = aboutDao.getLanguages();

        // then
        var languagesObject = new JSONObject(languages);
        assertThat(languagesObject.getInt("eng")).isEqualTo(123);
        assertThat(languagesObject.getInt("fra")).isEqualTo(456);
        assertThat(languagesObject.getInt("spa")).isEqualTo(789);
    }
}
