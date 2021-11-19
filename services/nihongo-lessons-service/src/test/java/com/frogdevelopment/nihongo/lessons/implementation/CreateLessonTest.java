package com.frogdevelopment.nihongo.lessons.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class CreateLessonTest {

    @InjectMocks
    private CreateLesson createLesson;

    @Mock
    private JapaneseDao japaneseDao;
    @Mock
    private TranslationDao translationDao;

    @Test
    void create() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .build();

        var french = Translation.builder()
                .japaneseId(japanese.getId())
                .lesson(1)
                .locale("fr_FR")
                .input("INPUT FRENCH")
                .sortLetter('I')
                .details("DETAILS FRENCH")
                .example("EXAMPLE FRENCH")
                .tag("TAGS FRENCH")
                .build();

        var english = Translation.builder()
                .japaneseId(japanese.getId())
                .lesson(1)
                .locale("en_US")
                .input("INPUT ENGLISH")
                .sortLetter('I')
                .details("DETAILS ENGLISH")
                .example("EXAMPLE ENGLISH")
                .tag("TAGS ENGLISH")
                .build();

        var inputDto = InputDto.builder()
                .japanese(japanese)
                .translation(french)
                .translation(english)
                .build();

        given(japaneseDao.create(japanese)).willReturn(123);
        given(translationDao.create(123, french)).willReturn(456);
        given(translationDao.create(123, english)).willReturn(789);

        // when
        var saveValue = createLesson.call(inputDto);

        // then
        assertThat(saveValue.getJapanese().getId()).isEqualTo(123);
        assertThat(saveValue.getTranslations())
                .extracting(Translation::getId)
                .containsOnly(456, 789);
    }

}
