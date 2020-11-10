package fr.frogdevelopment.nihongo.lesson.implementation;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import fr.frogdevelopment.nihongo.lesson.dao.JapaneseDao;
import fr.frogdevelopment.nihongo.lesson.dao.TranslationDao;
import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    public CreateLesson(JapaneseDao japaneseDao,
                        TranslationDao translationDao) {
        this.japaneseDao = japaneseDao;
        this.translationDao = translationDao;
    }

    @Transactional(propagation = REQUIRED)
    public InputDto call(InputDto inputDto) {

        japaneseDao.create(inputDto.getJapanese());

        inputDto.getTranslations().forEach(translation -> {
            translation.setJapaneseId(inputDto.getJapanese().getId());
            translationDao.create(translation);
        });

        return inputDto;
    }
}
