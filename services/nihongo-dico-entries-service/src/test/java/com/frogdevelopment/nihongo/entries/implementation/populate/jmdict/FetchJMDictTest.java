package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import com.frogdevelopment.nihongo.entries.implementation.populate.SaveData;
import com.frogdevelopment.nihongo.multischema.Language;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

//@Disabled("fixme ")
@Rollback
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
class FetchJMDictTest {

    @Autowired
    private FetchJMDict fetchJmDict;
    @Autowired
    private SaveData saveData;
    @Autowired
    private AboutDao aboutDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("classpath:jmdict_sample.txt")
    private Resource data;

    @Test
    void test() throws IOException {
        // given
        var date = "";
        try (var scanner = new Scanner(data.getInputStream(), UTF_8)) {
            date = fetchJmDict.read(scanner);
        }

        // when
        var map = saveData.call();
        aboutDao.insert(date, map);

        // then
        assertThat(date).isEqualTo("2019-02-01");
        thenEntriesAreCorrectlySaved();
        thenSensesAreCorrectlySaved();
        thenGlossesAreCorrectlySaved();

        var about = jdbcTemplate.queryForList("SELECT * FROM about");
        assertThat(about).hasSize(1);
        assertThat(about.get(0))
                .containsEntry("jmdict_date", "2019-02-01")
                .containsEntry("nb_entries", 1);
        assertThat(about.get(0).get("languages")).isInstanceOf(PGobject.class);
        var languages = (PGobject) about.get(0).get("languages");
        assertThat(languages.getType()).isEqualTo("json");
        assertThat(languages.getValue()).isEqualTo("{\"hun\":2,\"swe\":0,\"rus\":4,\"dut\":21,\"fra\":2,\"ger\":4,\"spa\":12,\"slv\":2,\"eng\":3}");
    }

    private void thenEntriesAreCorrectlySaved() {
        var entries = jdbcTemplate.queryForList("SELECT * FROM jpn.entries");
        assertThat(entries).hasSize(1);
        var entry = entries.get(0);
        assertThat(entry)
                .containsEntry("entry_seq", 1594720)
                .containsEntry("kanji", "収集")
                .containsEntry("kana", "しゅうしゅう")
                .containsEntry("reading", "shuushuu");
//        assertThat(entry.get("kanjis")).containsExactly("収集", "蒐集", "拾集", "収輯");
    }

    private void thenSensesAreCorrectlySaved() {
        var senses = jdbcTemplate.queryForList("SELECT * FROM jpn.senses");
        assertThat(senses).hasSize(12);
        for (var i = 0; i < 12; i++) {
            var sense = senses.get(i);
            assertThat(sense)
                    .containsEntry("sense_seq", "1594720_" + (i + 1))
                    .containsEntry("entry_seq", 1594720)
                    .containsEntry("pos", "n;vs")
                    .containsEntry("field", null)
                    .containsEntry("misc", null)
                    .containsEntry("info", null)
                    .containsEntry("dial", null);
        }
    }

    private void thenGlossesAreCorrectlySaved() {
        thenGlosses_DUT_AreCorrectlySaved();
        thenGlosses_ENG_AreCorrectlySaved();
        thenGlosses_FRA_AreCorrectlySaved();
        thenGlosses_GER_AreCorrectlySaved();
        thenGlosses_HUN_AreCorrectlySaved();
        thenGlosses_RUS_AreCorrectlySaved();
        thenGlosses_SLV_AreCorrectlySaved();
        thenGlosses_SPA_AreCorrectlySaved();
        thenGlosses_SWE_AreCorrectlySaved();
    }

    private void thenGlosses_DUT_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.DUT);
        assertThat(glosses).hasSize(21);
        thenGlossContains(glosses.get(0), "1594720_2", "verzamelen");
        thenGlossContains(glosses.get(1), "1594720_2", "bijeenbrengen");
        thenGlossContains(glosses.get(2), "1594720_2", "samenbrengen");
        thenGlossContains(glosses.get(3), "1594720_2", "vergaren");
        thenGlossContains(glosses.get(4), "1594720_2", "{veroud.} vergaderen");
        thenGlossContains(glosses.get(5), "1594720_2", "inzamelen");
        thenGlossContains(glosses.get(6), "1594720_2", "{ごみを} ophalen");
        thenGlossContains(glosses.get(7), "1594720_2", "schooien");
        thenGlossContains(glosses.get(8), "1594720_3", "collectioneren");
        thenGlossContains(glosses.get(9), "1594720_3", "verzamelen");
        thenGlossContains(glosses.get(10), "1594720_3", "sparen");
        thenGlossContains(glosses.get(11), "1594720_4", "verzameling");
        thenGlossContains(glosses.get(12), "1594720_4", "bijeenbrenging");
        thenGlossContains(glosses.get(13), "1594720_4", "samenbrenging");
        thenGlossContains(glosses.get(14), "1594720_4", "vergaring");
        thenGlossContains(glosses.get(15), "1594720_4", "{veroud.} vergadering");
        thenGlossContains(glosses.get(16), "1594720_4", "inzameling");
        thenGlossContains(glosses.get(17), "1594720_4", "{ごみの} ophaling");
        thenGlossContains(glosses.get(18), "1594720_5", "verzameling");
        thenGlossContains(glosses.get(19), "1594720_5", "collectie");
        thenGlossContains(glosses.get(20), "1594720_5", "bestand");
    }

    private void thenGlosses_ENG_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.ENG);
        assertThat(glosses).hasSize(3);
        thenGlossContains(glosses.get(0), "1594720_1", "gathering up");
        thenGlossContains(glosses.get(1), "1594720_1", "collection");
        thenGlossContains(glosses.get(2), "1594720_1", "accumulation");
    }

    private void thenGlosses_FRA_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.FRA);
        assertThat(glosses).hasSize(2);
        thenGlossContains(glosses.get(0), "1594720_6", "collection");
        thenGlossContains(glosses.get(1), "1594720_6", "récolte");
    }

    private void thenGlosses_GER_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.GER);
        assertThat(glosses).hasSize(4);
        thenGlossContains(glosses.get(0), "1594720_7", "sammeln");
        thenGlossContains(glosses.get(1), "1594720_7", "(f) Sammlung");
        thenGlossContains(glosses.get(2), "1594720_7", "(f) Kollektion");
        thenGlossContains(glosses.get(3), "1594720_7", "(n) Sammeln");
    }

    private void thenGlosses_HUN_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.HUN);
        assertThat(glosses).hasSize(2);
        thenGlossContains(glosses.get(0), "1594720_8", "gyűjtemény");
        thenGlossContains(glosses.get(1), "1594720_8", "gyűjtés");
    }

    private void thenGlosses_RUS_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.RUS);
        assertThat(glosses).hasSize(4);
        thenGlossContains(glosses.get(0), "1594720_10", "сбор");
        thenGlossContains(glosses.get(1), "1594720_10", "собирать, коллекционировать");
        thenGlossContains(glosses.get(2), "1594720_10", "собирание, коллекционирование");
        thenGlossContains(glosses.get(3), "1594720_10", "{～する} собирать, коллекционировать");
    }

    private void thenGlosses_SLV_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.SLV);
        assertThat(glosses).hasSize(2);
        thenGlossContains(glosses.get(0), "1594720_11", "zbiranje");
        thenGlossContains(glosses.get(1), "1594720_11", "zbirati");
    }

    private void thenGlosses_SPA_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.SPA);
        assertThat(glosses).hasSize(12);
        thenGlossContains(glosses.get(0), "1594720_12", "coleccionar");
        thenGlossContains(glosses.get(1), "1594720_12", "recopilar");
        thenGlossContains(glosses.get(2), "1594720_12", "hacer colección de");
        thenGlossContains(glosses.get(3), "1594720_12", "recoger");
        thenGlossContains(glosses.get(4), "1594720_12", "colección");
        thenGlossContains(glosses.get(5), "1594720_12", "amontonamiento");
        thenGlossContains(glosses.get(6), "1594720_12", "hacinamiento");
        thenGlossContains(glosses.get(7), "1594720_12", "acumulamiento");
        thenGlossContains(glosses.get(8), "1594720_12", "colección");
        thenGlossContains(glosses.get(9), "1594720_12", "recopilación");
        thenGlossContains(glosses.get(10), "1594720_12", "recolecta");
        thenGlossContains(glosses.get(11), "1594720_12", "recogida");
    }

    private void thenGlosses_SWE_AreCorrectlySaved() {
        var glosses = fetchGlosses(Language.SWE);
        assertThat(glosses).isEmpty();
    }

    private List<Map<String, Object>> fetchGlosses(Language language) {
        return jdbcTemplate.queryForList("SELECT * FROM " + language.getCode() + ".glosses");
    }

    private static void thenGlossContains(Map<String, Object> actual, String senseSeq, String vocabulary) {
        assertThat(actual).containsEntry("sense_seq", senseSeq).containsEntry("vocabulary", vocabulary);
    }
}
