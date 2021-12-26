package com.frogdevelopment.nihongo.lessons.application.manage;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
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
            if (translation.isToDelete()) {
                translationDao.deleteJapaneseTranslations(translation.getId());
            } else if (translation.getId() == 0) {
                final var translationId = translationDao.create(inputDto.getJapanese().getId(), translation);
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
