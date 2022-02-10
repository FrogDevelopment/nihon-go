package com.frogdevelopment.nihongo.lessons.dao.impl;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.frogdevelopment.nihongo.lessons.dao.InputDao;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integrationTest")
@Transactional(REQUIRED)
@MicronautTest(startApplication = false)
class InputDaoTest {

    @Inject
    private InputDao inputDao;

    @Test
    void fetch() {
        // given
        var lesson = 1;
        var sortField = "japanese_id";
        var sortOrder = "ASC";

        // when
        var dtos = inputDao.fetch(lesson, sortField, sortOrder);

        // then
        assertThat(dtos).hasSize(40);
        var inputDto = dtos.get(0);
        assertThat(inputDto.getJapanese()).isNotNull();
        assertThat(inputDto.getJapanese().getId()).isEqualTo(1);
        assertThat(inputDto.getJapanese().getKanji()).isEqualTo("私");
        assertThat(inputDto.getJapanese().getKana()).isEqualTo("わたし");
        assertThat(inputDto.getJapanese().getLesson()).isOne();

        assertThat(inputDto.getTranslations()).hasSize(2);

        final var enUs = inputDto.getTranslations().get("en_US");
        assertThat(enUs.getId()).isEqualTo(1);
        assertThat(enUs.getJapaneseId()).isEqualTo(1);
        assertThat(enUs.getLocale()).isEqualTo("en_US");
        assertThat(enUs.getInput()).isEqualTo("I");
        assertThat(enUs.getSortLetter()).isEqualTo('I');
        assertThat(enUs.getDetails()).isNullOrEmpty();
        assertThat(enUs.getExample()).isNullOrEmpty();

        final var frFr = inputDto.getTranslations().get("fr_FR");
        assertThat(frFr.getId()).isEqualTo(2);
        assertThat(frFr.getJapaneseId()).isEqualTo(1);
        assertThat(frFr.getLocale()).isEqualTo("fr_FR");
        assertThat(frFr.getInput()).isEqualTo("Je, moi");
        assertThat(frFr.getSortLetter()).isEqualTo('J');
        assertThat(frFr.getDetails()).isNullOrEmpty();
        assertThat(frFr.getExample()).isNullOrEmpty();
    }
}
