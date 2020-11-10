package fr.frogdevelopment.nihongo.lesson.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;

import fr.frogdevelopment.nihongo.lesson.dao.JapaneseDao;
import fr.frogdevelopment.nihongo.lesson.dao.TranslationDao;
import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import fr.frogdevelopment.nihongo.lesson.entity.Japanese;
import fr.frogdevelopment.nihongo.lesson.entity.Translation;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.VoidAnswer1;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class CreateLessonTest {

    @InjectMocks
    private CreateLesson createLesson;

    @Mock
    private JapaneseDao japaneseDao;
    @Mock
    private TranslationDao translationDao;

    @Captor
    private ArgumentCaptor<Translation> translationArgumentCaptor;

    @Test
    void create() {
        // given
        var japanese = Japanese.builder()
                .kanji("KANJI")
                .kana("KANA")
                .build();

        var french = Translation.builder()
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

        doAnswer(answerVoid((VoidAnswer1<Japanese>) japanese1 -> japanese1.setId(123)))
                .when(japaneseDao).create(japanese);

        // when
        createLesson.call(inputDto);

        // then
        then(japaneseDao)
                .should()
                .create(japanese);
        then(translationDao)
                .should(times(2))
                .create(translationArgumentCaptor.capture());
        List<Translation> allValues = translationArgumentCaptor.getAllValues();
        assertThat(allValues).hasSize(2);
        assertThat(allValues)
                .extracting(Translation::getJapaneseId)
                .containsOnly(japanese.getId());
    }

}
