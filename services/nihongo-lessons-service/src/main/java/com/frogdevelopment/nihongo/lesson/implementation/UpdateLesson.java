package com.frogdevelopment.nihongo.lesson.implementation;

import com.frogdevelopment.nihongo.lesson.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lesson.dao.TranslationDao;
import com.frogdevelopment.nihongo.lesson.entity.InputDto;
import com.frogdevelopment.nihongo.lesson.entity.Translation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Component
public class UpdateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    public UpdateLesson(JapaneseDao japaneseDao,
                        TranslationDao translationDao) {
        this.japaneseDao = japaneseDao;
        this.translationDao = translationDao;
    }

    @Transactional(propagation = REQUIRED)
    public InputDto call(InputDto inputDto) {

        japaneseDao.update(inputDto.getJapanese());

        Translation translation;
        var iterator = inputDto.getTranslations().iterator();
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
