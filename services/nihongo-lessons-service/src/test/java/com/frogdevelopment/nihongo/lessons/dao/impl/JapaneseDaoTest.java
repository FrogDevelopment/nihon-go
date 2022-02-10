package com.frogdevelopment.nihongo.lessons.dao.impl;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integrationTest")
@Transactional(REQUIRED)
@MicronautTest(startApplication = false)
class JapaneseDaoTest {

    @Inject
    private JapaneseDao japaneseDao;

    @Test
    void crud_should_work() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .lesson(1)
                .build();

        // ****** SAVE
        // when
        var id = japaneseDao.save(japanese).getId();

        // then
        var saved = japaneseDao.findById(id);
        assertThat(saved).isPresent();
        var savedJapanese = saved.get();
        assertThat(savedJapanese).extracting(Japanese::getKanji).isEqualTo("KANJI");
        assertThat(savedJapanese).extracting(Japanese::getKana).isEqualTo("KANA");
        assertThat(savedJapanese).extracting(Japanese::getLesson).isEqualTo(1);

        // ****** UPDATE
        // given
        var toUpdate = savedJapanese.toBuilder().kana("new KANA").build();

        // when
        japaneseDao.update(toUpdate);

        // then
        var updated = japaneseDao.findById(id);
        assertThat(updated).isPresent();
        var updatedJapanese = updated.get();
        assertThat(updatedJapanese).extracting(Japanese::getKanji).isEqualTo("KANJI");
        assertThat(updatedJapanese).extracting(Japanese::getKana).isEqualTo("new KANA");
        assertThat(updatedJapanese).extracting(Japanese::getLesson).isEqualTo(1);

        // ****** DELETE
        // when
        japaneseDao.deleteById(id);

        // then
        var exists = japaneseDao.existsById(id);
        assertThat(exists).isFalse();
    }
}
