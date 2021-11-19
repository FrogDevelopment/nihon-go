package com.frogdevelopment.nihongo.lessons.implementation;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

@Component
@RequiredArgsConstructor
public class UpdateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    @Transactional(propagation = REQUIRED)
    public InputDto call(final InputDto inputDto) {
        final var inputDtoBuilder = InputDto.builder();

        japaneseDao.update(inputDto.getJapanese());
        inputDtoBuilder.japanese(inputDto.getJapanese());

        for (final Translation translation : inputDto.getTranslations()) {
            if (translation.isToDelete()) {
                translationDao.delete(translation.getId());
            } else if (translation.getId() == 0) {
                final var translationId = translationDao.create(inputDto.getJapanese().getId(), translation);
                inputDtoBuilder.translation(translation.toBuilder().id(translationId).build());
            } else {
                translationDao.update(translation);
                inputDtoBuilder.translation(translation);
            }
        }

        return inputDtoBuilder.build();
    }
}
