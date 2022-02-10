package com.frogdevelopment.nihongo.lessons.dao.impl;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integrationTest")
@Transactional(REQUIRED)
@MicronautTest(startApplication = false)
class TranslationDaoTest {

    @Inject
    private TranslationDao translationDao;

    @Test
    void crud_should_work() {
        // given
        var translation = Translation.builder()
                .japaneseId(1L)
                .locale("LOCALE")
                .input("   INPUT    ")
                .details("   DETAILS   ")
                .example("   EXAMPLE   ")
                .build();

        // ****** SAVE
        // when
        var id = translationDao.save(translation).getId();

        // then
        var saved = translationDao.findById(id);
        assertThat(saved).isPresent();
        var savedTranslation = saved.get();
        assertThat(savedTranslation).extracting(Translation::getJapaneseId).isEqualTo(1L);
        assertThat(savedTranslation).extracting(Translation::getLocale).isEqualTo("LOCALE");
        assertThat(savedTranslation).extracting(Translation::getInput).isEqualTo("INPUT");
        assertThat(savedTranslation).extracting(Translation::getDetails).isEqualTo("DETAILS");
        assertThat(savedTranslation).extracting(Translation::getExample).isEqualTo("EXAMPLE");

        // ****** UPDATE
        // given
        var toUpdate = savedTranslation.toBuilder().input("new INPUT").build();

        // when
        translationDao.update(toUpdate);

        // then
        var updated = translationDao.findById(id);
        assertThat(updated).isPresent();
        var updatedTranslation = updated.get();
        assertThat(updatedTranslation).extracting(Translation::getJapaneseId).isEqualTo(1L);
        assertThat(updatedTranslation).extracting(Translation::getLocale).isEqualTo("LOCALE");
        assertThat(updatedTranslation).extracting(Translation::getInput).isEqualTo("new INPUT");
        assertThat(updatedTranslation).extracting(Translation::getDetails).isEqualTo("DETAILS");
        assertThat(updatedTranslation).extracting(Translation::getExample).isEqualTo("EXAMPLE");

        // ****** DELETE
        // when
        translationDao.deleteById(id);

        // then
        var exists = translationDao.existsById(id);
        assertThat(exists).isFalse();
    }

    @Test
    void deleteByJapaneseId_should_() {
        // given

        // when
        var deleted = translationDao.deleteByJapaneseId(1L);

        // then
        assertThat(deleted).isEqualTo(2);
    }
}

