package com.frogdevelopment.nihongo.lessons.application.manage;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import jakarta.inject.Singleton;

@Singleton
@RequiredArgsConstructor
class DeleteLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;
    private final LessonDao lessonDao;

    void call(final InputDto inputDto) {
        if (inputDto.getJapanese().getId() != null) {

            translationDao.deleteByJapaneseId(inputDto.getJapanese().getId());

            japaneseDao.delete(inputDto.getJapanese());

            lessonDao.deleteById(inputDto.getJapanese().getLesson());
        }
    }

}
