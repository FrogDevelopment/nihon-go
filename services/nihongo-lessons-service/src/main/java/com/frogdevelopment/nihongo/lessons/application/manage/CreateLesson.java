package com.frogdevelopment.nihongo.lessons.application.manage;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CreateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;
    private final LessonDao lessonDao;

    InputDto call(final InputDto inputDto) {

        final var japaneseId = japaneseDao.create(inputDto.getJapanese());

        final var inputDtoBuilder = InputDto.builder()
                .japanese(inputDto.getJapanese().toBuilder().id(japaneseId).build());

        inputDto.getTranslations().forEach((key, translation) -> {
            final var translationId = translationDao.create(japaneseId, translation);
            inputDtoBuilder.translation(key, translation.toBuilder().id(translationId).build());
        });

        lessonDao.upsertLesson(inputDto.getJapanese().getLesson());

        return inputDtoBuilder.build();
    }
}
