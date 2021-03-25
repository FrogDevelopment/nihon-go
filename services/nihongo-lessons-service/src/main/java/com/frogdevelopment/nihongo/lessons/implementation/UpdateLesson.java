package com.frogdevelopment.nihongo.lessons.implementation;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Translation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Component
@RequiredArgsConstructor
public class UpdateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    @Transactional(propagation = REQUIRED)
    public InputDto call(final InputDto inputDto) {

        japaneseDao.update(inputDto.getJapanese());

        Translation translation;
        final var iterator = inputDto.getTranslations().iterator();
        while (iterator.hasNext()) {
            translation = iterator.next();

            translation.setJapaneseId(inputDto.getJapanese().getId());
            if (translation.isToDelete()) {
                translationDao.delete(translation);
                iterator.remove();
            } else if (translation.getId() == 0) {
                translation.setJapaneseId(inputDto.getJapanese().getId());
                translationDao.create(translation);
            } else {
                translationDao.update(translation);
            }
        }

        return inputDto;
    }
}
