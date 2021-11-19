package com.frogdevelopment.nihongo.lessons.application.manage;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

@Component
@RequiredArgsConstructor
class CreateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    InputDto call(final InputDto inputDto) {

        final var japaneseId = japaneseDao.create(inputDto.getJapanese());

        final var inputDtoBuilder = InputDto.builder()
                .japanese(inputDto.getJapanese().toBuilder().id(japaneseId).build());

        inputDto.getTranslations().forEach(translation -> {
            final var translationId = translationDao.create(japaneseId, translation);
            inputDtoBuilder.translation(translation.toBuilder().id(translationId).build());
        });

        return inputDtoBuilder.build();
    }
}
