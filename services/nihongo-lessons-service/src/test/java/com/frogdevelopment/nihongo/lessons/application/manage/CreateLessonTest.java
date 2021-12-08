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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class CreateLessonTest {

    @InjectMocks
    private CreateLesson createLesson;

    @Mock
    private JapaneseDao japaneseDao;
    @Mock
    private TranslationDao translationDao;
    @Mock
    private LessonDao lessonDao;

    @Test
    void create() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .lesson(1)
                .build();

        var french = Translation.builder()
                .japaneseId(japanese.getId())
                .locale("fr_FR")
                .input("INPUT FRENCH")
                .sortLetter('I')
                .details("DETAILS FRENCH")
                .example("EXAMPLE FRENCH")
                .build();

        var english = Translation.builder()
                .japaneseId(japanese.getId())
                .locale("en_US")
                .input("INPUT ENGLISH")
                .sortLetter('I')
                .details("DETAILS ENGLISH")
                .example("EXAMPLE ENGLISH")
                .build();

        var inputDto = InputDto.builder()
                .japanese(japanese)
                .translation(french.getLocale(), french)
                .translation(english.getLocale(), english)
                .build();

        given(japaneseDao.create(japanese)).willReturn(123);
        given(translationDao.create(123, french)).willReturn(456);
        given(translationDao.create(123, english)).willReturn(789);

        // when
        var saveValue = createLesson.call(inputDto);

        // then
        assertThat(saveValue.getJapanese().getId()).isEqualTo(123);
        assertThat(saveValue.getTranslations().values())
                .extracting(Translation::getId)
                .containsOnly(456, 789);
        then(lessonDao)
                .should()
                .upsertLesson(japanese.getLesson());
    }

}
