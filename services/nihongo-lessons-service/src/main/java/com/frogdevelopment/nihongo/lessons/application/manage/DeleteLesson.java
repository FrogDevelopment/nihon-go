package com.frogdevelopment.nihongo.lessons.application.manage;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

@Component
@RequiredArgsConstructor
class DeleteLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    void call(final InputDto inputDto) {
        if (inputDto.getJapanese().getId() != 0) {

            inputDto.getTranslations().forEach(this::delete);

            japaneseDao.delete(inputDto.getJapanese());
        }
    }

    private void delete(final Translation translation) {
        if (translation.getId() != 0) {
            translationDao.delete(translation.getId());
        }
    }
}
