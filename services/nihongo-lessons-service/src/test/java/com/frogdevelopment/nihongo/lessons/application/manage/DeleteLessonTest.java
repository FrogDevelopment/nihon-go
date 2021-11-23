package com.frogdevelopment.nihongo.lessons.application.manage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

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
class DeleteLessonTest {

    @InjectMocks
    private DeleteLesson deleteLesson;

    @Mock
    private JapaneseDao japaneseDao;
    @Mock
    private TranslationDao translationDao;

    @Test
    void should_delete() {
        // given
        var japanese = Japanese.builder()
                .id(123)
                .kanji("KANJI")
                .kana("KANA")
                .lesson(1)
                .build();

        var french = Translation.builder()
                .id(456)
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
                .translation(french)
                .translation(english)
                .build();

        // when
        deleteLesson.call(inputDto);

        // then
        then(japaneseDao)
                .should()
                .delete(japanese);
        then(translationDao)
                .should(times(1))
                .delete(french.getId());
    }

    @Test
    void should_doNothing_when_japaneseIdEquals0() {
        // given
        var inputDto = InputDto.builder()
                .japanese(Japanese.builder()
                        .kana("KANA")
                        .lesson(1)
                        .build())
                .build();

        // when
        deleteLesson.call(inputDto);

        // then
        then(japaneseDao)
                .should(never())
                .delete(any());
        then(translationDao)
                .should(never())
                .delete(anyInt());
    }

}
