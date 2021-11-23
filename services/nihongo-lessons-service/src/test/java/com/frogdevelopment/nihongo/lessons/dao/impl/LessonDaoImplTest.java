package com.frogdevelopment.nihongo.lessons.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
@Rollback
@Transactional(propagation = Propagation.REQUIRED)
class LessonDaoImplTest {

    @Autowired
    private LessonDaoImpl lessonDao;

    @Test
    void getTotal() {
        // when
        var total = lessonDao.getTotal();

        // then
        assertThat(total).isEqualTo(500);
    }

    @Test
    void fetch() {
        // given
        var pageIndex = 1;
        var pageSize = 100;
        var sortField = "japanese_id";
        var sortOrder = "ASC";

        // when
        var dtos = lessonDao.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        assertThat(dtos).hasSize(100);
        var inputDto = dtos.get(0);
        assertThat(inputDto.getJapanese()).isNotNull();
        assertThat(inputDto.getJapanese().getId()).isEqualTo(1);
        assertThat(inputDto.getJapanese().getKanji()).isEqualTo("私");
        assertThat(inputDto.getJapanese().getKana()).isEqualTo("わたし");
        assertThat(inputDto.getJapanese().getLesson()).isOne();

        assertThat(inputDto.getTranslations()).hasSize(2);
        assertThat(inputDto.getTranslations().get(0).getId()).isEqualTo(1);
        assertThat(inputDto.getTranslations().get(0).getJapaneseId()).isEqualTo(1);
        assertThat(inputDto.getTranslations().get(0).getLocale()).isEqualTo("en_US");
        assertThat(inputDto.getTranslations().get(0).getInput()).isEqualTo("I");
        assertThat(inputDto.getTranslations().get(0).getSortLetter()).isEqualTo('I');
        assertThat(inputDto.getTranslations().get(0).getDetails()).isNullOrEmpty();
        assertThat(inputDto.getTranslations().get(0).getExample()).isNullOrEmpty();

        assertThat(inputDto.getTranslations().get(1).getId()).isEqualTo(2);
        assertThat(inputDto.getTranslations().get(1).getJapaneseId()).isEqualTo(1);
        assertThat(inputDto.getTranslations().get(1).getLocale()).isEqualTo("fr_FR");
        assertThat(inputDto.getTranslations().get(1).getInput()).isEqualTo("Je, moi");
        assertThat(inputDto.getTranslations().get(1).getSortLetter()).isEqualTo('J');
        assertThat(inputDto.getTranslations().get(1).getDetails()).isNullOrEmpty();
        assertThat(inputDto.getTranslations().get(1).getExample()).isNullOrEmpty();
    }

}
