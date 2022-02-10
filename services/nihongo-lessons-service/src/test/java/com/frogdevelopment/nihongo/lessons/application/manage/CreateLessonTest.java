package com.frogdevelopment.nihongo.lessons.application.manage;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer1;
import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.answer;
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
                .sortLetter("I")
                .details("DETAILS FRENCH")
                .example("EXAMPLE FRENCH")
                .build();

        var english = Translation.builder()
                .japaneseId(japanese.getId())
                .locale("en_US")
                .input("INPUT ENGLISH")
                .sortLetter("I")
                .details("DETAILS ENGLISH")
                .example("EXAMPLE ENGLISH")
                .build();

        var inputDto = InputDto.builder()
                .japanese(japanese)
                .translation(french.getLocale(), french)
                .translation(english.getLocale(), english)
                .build();

        given(japaneseDao.save(japanese)).will(answer((Answer1<Japanese, Japanese>) argument -> argument.toBuilder().id(123L).build()));
        given(translationDao.save(french)).will(answer((Answer1<Translation, Translation>) argument -> argument.toBuilder().id(456L).build()));
        given(translationDao.save(english)).will(answer((Answer1<Translation, Translation>) argument -> argument.toBuilder().id(789L).build()));

        // when
        var saveValue = createLesson.call(inputDto);

        // then
        assertThat(saveValue.getJapanese().getId()).isEqualTo(123L);
        assertThat(saveValue.getTranslations().values())
                .extracting(Translation::getId)
                .containsOnly(456L, 789L);
        then(lessonDao)
                .should()
                .upsertLesson(japanese.getLesson());
    }

}
