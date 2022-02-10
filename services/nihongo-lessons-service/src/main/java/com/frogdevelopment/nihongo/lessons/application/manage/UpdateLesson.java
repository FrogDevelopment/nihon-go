package com.frogdevelopment.nihongo.lessons.application.manage;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import jakarta.inject.Singleton;

@Singleton
@RequiredArgsConstructor
class UpdateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;
    private final LessonDao lessonDao;

    InputDto call(final InputDto inputDto) {
        final var inputDtoBuilder = InputDto.builder();

        japaneseDao.update(inputDto.getJapanese());
        inputDtoBuilder.japanese(inputDto.getJapanese());

        inputDto.getTranslations().forEach((key, translation) -> {
            /*if (translation.isToDelete()) {
                translationDao.deleteByJapaneseId(translation.getJapaneseId());
            } else*/
            if (translation.getId() == 0) {
                translation.setJapaneseId(inputDto.getJapanese().getId());
                final var translationId = translationDao.save(translation).getId();
                inputDtoBuilder.translation(key, translation.toBuilder().id(translationId).build());
            } else {
                translationDao.update(translation);
                inputDtoBuilder.translation(key, translation);
            }
        });

        lessonDao.upsertLesson(inputDto.getJapanese().getLesson());

        return inputDtoBuilder.build();
    }
}
