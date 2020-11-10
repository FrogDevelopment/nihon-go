package fr.frogdevelopment.nihongo.lesson.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import fr.frogdevelopment.nihongo.lesson.dao.JapaneseDao;
import fr.frogdevelopment.nihongo.lesson.dao.TranslationDao;
import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import fr.frogdevelopment.nihongo.lesson.entity.Japanese;
import fr.frogdevelopment.nihongo.lesson.entity.Translation;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class DeleteLessonTest {

    @InjectMocks
    private DeleteLesson deleteLesson;

    @Mock
    private JapaneseDao japaneseDao;
    @Mock
    private TranslationDao translationDao;

    @Captor
    private ArgumentCaptor<Translation> translationArgumentCaptor;

    @Test
    void delete() {
        // given
        var japanese = Japanese.builder()
                .id(123)
                .kanji("KANJI")
                .kana("KANA")
                .build();

        var french = Translation.builder()
                .id(456)
                .japaneseId(japanese.getId())
                .locale("fr_FR")
                .input("INPUT FRENCH")
                .details("DETAILS FRENCH")
                .example("EXAMPLE FRENCH")
                .tag("TAGS FRENCH*")
                .build();

        var english = Translation.builder()
                .japaneseId(japanese.getId())
                .locale("en_US")
                .input("INPUT ENGLISH")
                .details("DETAILS ENGLISH")
                .example("EXAMPLE ENGLISH")
                .tag("TAGS ENGLISH")
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
                .delete(translationArgumentCaptor.capture());
        Translation deletedValue = translationArgumentCaptor.getValue();
        assertThat(deletedValue).extracting(Translation::getId).isEqualTo(french.getId());
    }

    @Test
    void delete_no_japanese_id() {
        // given
        var inputDto = InputDto.builder()
                .japanese(Japanese.builder()
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
                .delete(any());
    }

}
