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
            translationDao.delete(translation.getId());
        }
    }
}
