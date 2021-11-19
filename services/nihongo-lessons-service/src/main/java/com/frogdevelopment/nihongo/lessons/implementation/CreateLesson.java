package com.frogdevelopment.nihongo.lessons.implementation;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

@Component
@RequiredArgsConstructor
public class CreateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    @Transactional(propagation = REQUIRED)
    public InputDto call(final InputDto inputDto) {

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
