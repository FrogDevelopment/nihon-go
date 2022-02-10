package com.frogdevelopment.nihongo.lessons.dao.impl;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.entity.LessonInfo;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integrationTest")
@Transactional(REQUIRED)
@MicronautTest(startApplication = false)
class LessonDaoTest {

    @Inject
    private LessonDao lessonDao;

    @Test
    void upsert_should_insert() {
        // given
        var byLesson = lessonDao.findById(2);
        assertThat(byLesson).isNotPresent();

        // when
        lessonDao.upsertLesson(2);

        // then
        byLesson = lessonDao.findById(2);
        assertThat(byLesson).isPresent();
    }

    @Test
    void upsert_should_update() {
        // given
        var byLesson = lessonDao.findById(1);
        assertThat(byLesson).isPresent()
                .get()
                .extracting(LessonInfo::updateDateTime)
                .isEqualTo("2022-01-01 08:00:00");

        // when
        lessonDao.upsertLesson(1);

        // then
        byLesson = lessonDao.findById(1);
        assertThat(byLesson).isPresent()
                .get()
                .extracting(LessonInfo::updateDateTime)
                .isNotEqualTo("2022-01-01 08:00:00");
    }

    @Test
    void updateExportTime_should_update_exportTime() {
        // given
        var byLesson = lessonDao.findById(1);
        assertThat(byLesson).isPresent()
                .get()
                .extracting(LessonInfo::exportDateTime)
                .isNull();

        // when
        lessonDao.updateExportTime(1);

        // then
        byLesson = lessonDao.findById(1);
        assertThat(byLesson).isPresent()
                .get()
                .extracting(LessonInfo::exportDateTime)
                .isNotNull();
    }

}
