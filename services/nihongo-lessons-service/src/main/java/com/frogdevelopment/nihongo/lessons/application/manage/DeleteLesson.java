package com.frogdevelopment.nihongo.lessons.application.manage;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DeleteLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;
    private final LessonDao lessonDao;

    void call(final InputDto inputDto) {
        if (inputDto.getJapanese().getId() != 0) {

            translationDao.deleteJapaneseTranslations(inputDto.getJapanese().getId());

            japaneseDao.delete(inputDto.getJapanese());

            lessonDao.deleteLesson(inputDto.getJapanese().getLesson());
        }
    }

}
