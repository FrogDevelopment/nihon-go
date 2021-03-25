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
public class DeleteLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    @Transactional(propagation = REQUIRED)
    public void call(final InputDto inputDto) {
        if (inputDto.getJapanese().getId() != 0) {

            inputDto.getTranslations().forEach(this::delete);

            japaneseDao.delete(inputDto.getJapanese());
        }
    }

    private void delete(final Translation translation) {
        if (translation.getId() != 0) {
            translationDao.delete(translation);
        }
    }
}
