package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Disabled("fixme ")
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
public class FetchJMDictTest {

    @Autowired
    private FetchJMDict fetchJmDict;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("classpath:jmdict_sample.txt")
    private Resource data;

    @Test
    public void test() throws IOException {

        try (Scanner scanner = new Scanner(data.getInputStream(), UTF_8)) {
            fetchJmDict.read(scanner);
        }

        List<Map<String, Object>> entries = jdbcTemplate.queryForList("SELECT * FROM entries");
        assertThat(entries).isNotEmpty();

        List<Map<String, Object>> senses = jdbcTemplate.queryForList("SELECT * FROM senses");
        assertThat(senses).isNotEmpty();

        List<Map<String, Object>> glosses = jdbcTemplate.queryForList("SELECT * FROM glosses");
        assertThat(glosses).isNotEmpty();
//        List<Entry> entries = entryArgumentCaptor.getAllValues();
//
//        assertThat(entries).hasSize(1);
//        Entry entry = entries.get(0);
//        assertThat(entry.getSeq()).isEqualTo(1594720);
//        assertThat(entry.getKanji()).isEqualTo("収集");
//        assertThat(entry.getKanjis()).containsExactly("収集", "蒐集", "拾集", "収輯");
//        assertThat(entry.getReading()).isEqualTo("しゅうしゅう");
//
//        List<Sense> senses = entry.getSenses();
//        assertThat(senses).hasSize(12);
//
//        assertThat(senses.get(0).getField()).isEmpty();
//        assertThat(senses.get(0).getPos()).hasSize(2);
//        assertThat(senses.get(0).getPos()).containsExactly("n", "vs");
//        assertThat(senses.get(0).getMisc()).isEmpty();
//        assertThat(senses.get(0).getInfo()).isNull();
//        assertThat(senses.get(0).getDial()).isEmpty();
//        assertThat(senses.get(0).getGloss()).hasSize(3);
//        assertThat(senses.get(0).getGloss().get(0).getLang()).isEqualTo("eng");
//        assertThat(senses.get(0).getGloss().get(0).getValue()).isEqualTo("gathering up");
//        assertThat(senses.get(0).getGloss().get(1).getLang()).isEqualTo("eng");
//        assertThat(senses.get(0).getGloss().get(1).getValue()).isEqualTo("collection");
//        assertThat(senses.get(0).getGloss().get(2).getLang()).isEqualTo("eng");
//        assertThat(senses.get(0).getGloss().get(2).getValue()).isEqualTo("accumulation");
//
//        assertThat(senses.get(1).getField()).isEmpty();
//        assertThat(senses.get(1).getPos()).isEmpty();
//        assertThat(senses.get(1).getMisc()).isEmpty();
//        assertThat(senses.get(1).getInfo()).isNull();
//        assertThat(senses.get(1).getDial()).isEmpty();
//        assertThat(senses.get(1).getGloss()).hasSize(8);
//        assertThat(senses.get(1).getGloss().get(0).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(0).getValue()).isEqualTo("verzamelen");
//        assertThat(senses.get(1).getGloss().get(1).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(1).getValue()).isEqualTo("bijeenbrengen");
//        assertThat(senses.get(1).getGloss().get(2).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(2).getValue()).isEqualTo("samenbrengen");
//        assertThat(senses.get(1).getGloss().get(3).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(3).getValue()).isEqualTo("vergaren");
//        assertThat(senses.get(1).getGloss().get(4).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(4).getValue()).isEqualTo("{veroud.} vergaderen");
//        assertThat(senses.get(1).getGloss().get(5).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(5).getValue()).isEqualTo("inzamelen");
//        assertThat(senses.get(1).getGloss().get(6).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(6).getValue()).isEqualTo("{ごみを} ophalen");
//        assertThat(senses.get(1).getGloss().get(7).getLang()).isEqualTo("dut");
//        assertThat(senses.get(1).getGloss().get(7).getValue()).isEqualTo("schooien");
//
//        assertThat(senses.get(2).getField()).isEmpty();
//        assertThat(senses.get(2).getPos()).isEmpty();
//        assertThat(senses.get(2).getMisc()).isEmpty();
//        assertThat(senses.get(2).getInfo()).isNull();
//        assertThat(senses.get(2).getDial()).isEmpty();
//        assertThat(senses.get(2).getGloss()).hasSize(3);
//        assertThat(senses.get(2).getGloss().get(0).getLang()).isEqualTo("dut");
//        assertThat(senses.get(2).getGloss().get(0).getValue()).isEqualTo("collectioneren");
//        assertThat(senses.get(2).getGloss().get(1).getLang()).isEqualTo("dut");
//        assertThat(senses.get(2).getGloss().get(1).getValue()).isEqualTo("verzamelen");
//        assertThat(senses.get(2).getGloss().get(2).getLang()).isEqualTo("dut");
//        assertThat(senses.get(2).getGloss().get(2).getValue()).isEqualTo("sparen");
//
//        assertThat(senses.get(5).getField()).isEmpty();
//        assertThat(senses.get(5).getPos()).isEmpty();
//        assertThat(senses.get(5).getMisc()).isEmpty();
//        assertThat(senses.get(5).getInfo()).isNull();
//        assertThat(senses.get(5).getDial()).isEmpty();
//        assertThat(senses.get(5).getGloss()).hasSize(2);
//        assertThat(senses.get(5).getGloss().get(0).getLang()).isEqualTo("fra");
//        assertThat(senses.get(5).getGloss().get(0).getValue()).isEqualTo("collection");
//        assertThat(senses.get(5).getGloss().get(1).getLang()).isEqualTo("fra");
//        assertThat(senses.get(5).getGloss().get(1).getValue()).isEqualTo("récolte");
//
//        assertThat(senses.get(9).getField()).isEmpty();
//        assertThat(senses.get(9).getPos()).isEmpty();
//        assertThat(senses.get(9).getMisc()).isEmpty();
//        assertThat(senses.get(9).getInfo()).isNull();
//        assertThat(senses.get(9).getDial()).isEmpty();
//        assertThat(senses.get(9).getGloss()).hasSize(4);
//        assertThat(senses.get(9).getGloss().get(0).getLang()).isEqualTo("rus");
//        assertThat(senses.get(9).getGloss().get(0).getValue()).isEqualTo("сбор");
//        assertThat(senses.get(9).getGloss().get(1).getLang()).isEqualTo("rus");
//        assertThat(senses.get(9).getGloss().get(1).getValue()).isEqualTo("собирать, коллекционировать");
//        assertThat(senses.get(9).getGloss().get(2).getLang()).isEqualTo("rus");
//        assertThat(senses.get(9).getGloss().get(2).getValue()).isEqualTo("собирание, коллекционирование");
//        assertThat(senses.get(9).getGloss().get(3).getLang()).isEqualTo("rus");
//        assertThat(senses.get(9).getGloss().get(3).getValue()).isEqualTo("{～する} собирать, коллекционировать");

//        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM jmdict_info");
//        assertThat(maps).hasSize(1);
//        Map<String, Object> data = maps.get(0);
//        assertThat(data.get("date_created")).isEqualTo("2019-02-01");
//        assertThat(data.get("nb_entries")).isEqualTo(1);
//        assertThat(data.get("languages")).isEqualTo("dut=21;spa=12;ita=6;ger=4;rus=4;eng=3;fra=2;hun=2;slv=2");
    }
}
