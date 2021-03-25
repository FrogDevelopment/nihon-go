package com.frogdevelopment.nihongo.lessons.implementation;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Component
@RequiredArgsConstructor
public class CreateLesson {

    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    @Transactional(propagation = REQUIRED)
    public InputDto call(final InputDto inputDto) {

        japaneseDao.create(inputDto.getJapanese());

        inputDto.getTranslations().forEach(translation -> {
            translation.setJapaneseId(inputDto.getJapanese().getId());
            translationDao.create(translation);
        });

        return inputDto;
    }
}
