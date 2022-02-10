package com.frogdevelopment.nihongo.lessons.application.manage;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import jakarta.inject.Singleton;

@Singleton
@RequiredArgsConstructor
class CreateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;
    private final LessonDao lessonDao;

    InputDto call(final InputDto inputDto) {

        final var japaneseId = japaneseDao.save(inputDto.getJapanese()).getId();

        final var inputDtoBuilder = InputDto.builder()
                .japanese(inputDto.getJapanese().toBuilder().id(japaneseId).build());

        inputDto.getTranslations().forEach((key, translation) -> {
            translation.setJapaneseId(japaneseId);
            final var translationId = translationDao.save(translation).getId();
            inputDtoBuilder.translation(key, translation.toBuilder().id(translationId).build());
        });

        lessonDao.upsertLesson(inputDto.getJapanese().getLesson());

        return inputDtoBuilder.build();
    }
}
