package com.frogdevelopment.nihongo.lessons.application.manage;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;
import com.frogdevelopment.nihongo.lessons.entity.Translation;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class UpdateLessonTest {

    @InjectMocks
    private UpdateLesson updateLesson;

    @Mock
    private JapaneseDao japaneseDao;
    @Mock
    private TranslationDao translationDao;
    @Mock
    private LessonDao lessonDao;

    @Captor
    private ArgumentCaptor<Translation> translationArgumentCaptor;

    @Test
    void update() {
        // given
        var japanese = Japanese.builder()
                .id(123)
                .kanji("KANJI")
                .kana("KANA")
                .lesson(1)
                .build();

        var toUpdate = Translation.builder()
                .japaneseId(japanese.getId())
                .id(456)
                .locale("fr_FR")
                .input("INPUT FRENCH")
                .details("DETAILS FRENCH")
                .example("EXAMPLE FRENCH")
                .build();

        var toInsert = Translation.builder()
                .japaneseId(japanese.getId())
                .locale("en_US")
                .input("INPUT ENGLISH")
                .details("DETAILS ENGLISH")
                .example("EXAMPLE ENGLISH")
                .build();

        var toDelete = Translation.builder()
                .japaneseId(japanese.getId())
                .id(798)
                .toDelete(true)
                .build();

        var inputDto = InputDto.builder()
                .japanese(japanese)
                .translation(toUpdate.getLocale(), toUpdate)
                .translation(toInsert.getLocale(), toInsert)
                .translation(toDelete.getLocale(), toDelete)
                .build();

        // when
        updateLesson.call(inputDto);

        // then
        then(japaneseDao)
                .should()
                .update(japanese);
        then(translationDao)
                .should()
                .create(eq(123), translationArgumentCaptor.capture());
        var insertedValue = translationArgumentCaptor.getValue();
        assertThat(insertedValue).extracting(Translation::getJapaneseId).isEqualTo(japanese.getId());

        then(translationDao)
                .should()
                .update(translationArgumentCaptor.capture());
        var updatedValue = translationArgumentCaptor.getValue();
        assertThat(updatedValue).extracting(Translation::getId).isEqualTo(toUpdate.getId());

        then(translationDao)
                .should()
                .deleteJapaneseTranslations(toDelete.getId());

        then(lessonDao)
                .should()
                .upsertLesson(japanese.getLesson());
    }
}
