package fr.frogdevelopment.nihongo.lesson.implementation;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import fr.frogdevelopment.nihongo.lesson.dao.JapaneseDao;
import fr.frogdevelopment.nihongo.lesson.dao.TranslationDao;
import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import fr.frogdevelopment.nihongo.lesson.entity.Translation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    public DeleteLesson(JapaneseDao japaneseDao,
                        TranslationDao translationDao) {
        this.japaneseDao = japaneseDao;
        this.translationDao = translationDao;
    }

    @Transactional(propagation = REQUIRED)
    public void call(InputDto inputDto) {
        if (inputDto.getJapanese().getId() != 0) {

            inputDto.getTranslations().forEach(this::delete);

            japaneseDao.delete(inputDto.getJapanese());
        }
    }

    private void delete(Translation translation) {
        if (translation.getId() != 0) {
            translationDao.delete(translation);
        }
    }
}
