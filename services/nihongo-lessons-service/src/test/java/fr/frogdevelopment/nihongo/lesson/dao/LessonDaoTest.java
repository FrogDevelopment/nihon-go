package fr.frogdevelopment.nihongo.lesson.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
@Rollback
@Transactional(propagation = Propagation.REQUIRED)
class LessonDaoTest {

    @Autowired
    private LessonDao lessonDao;

    @Test
    void getLesson() {
        // when
        var lessons = lessonDao.getLesson("fr_FR", "01");

        // then
        assertThat(lessons).hasSize(40);
        var data = lessons.get(0);

        assertThat(data.getKanji()).isEqualTo("私");
        assertThat(data.getKana()).isEqualTo("わたし");
        assertThat(data.getSortLetter()).isEqualTo("J");
        assertThat(data.getInput()).isEqualTo("Je, moi");
        assertThat(data.getDetails()).isNull();
        assertThat(data.getExample()).isNull();
        assertThat(data.getTags()).isEqualTo("leçon 01,leçon A");
    }

    @Test
    void getTotal() {
        // when
        var total = lessonDao.getTotal();

        // then
        assertThat(total).isEqualTo(500);
    }

    @Test
    void fetch() {
        // given
        var pageIndex = 1;
        var pageSize = 100;
        var sortField = "japanese_id";
        var sortOrder = "ASC";

        // when
        var dtos = lessonDao.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        assertThat(dtos).hasSize(100);
        var inputDto = dtos.get(0);
        assertThat(inputDto.getJapanese()).isNotNull();
        assertThat(inputDto.getJapanese().getId()).isEqualTo(1);
        assertThat(inputDto.getJapanese().getKanji()).isEqualTo("私");
        assertThat(inputDto.getJapanese().getKana()).isEqualTo("わたし");

        assertThat(inputDto.getTranslations()).hasSize(2);
        assertThat(inputDto.getTranslations().get(0).getId()).isEqualTo(1);
        assertThat(inputDto.getTranslations().get(0).getJapaneseId()).isEqualTo(1);
        assertThat(inputDto.getTranslations().get(0).getLocale()).isEqualTo("en_US");
        assertThat(inputDto.getTranslations().get(0).getInput()).isEqualTo("I");
        assertThat(inputDto.getTranslations().get(0).getSortLetter()).isEqualTo("I");
        assertThat(inputDto.getTranslations().get(0).getDetails()).isNullOrEmpty();
        assertThat(inputDto.getTranslations().get(0).getExample()).isNullOrEmpty();
        assertThat(inputDto.getTranslations().get(0).getTags()).containsExactly("lesson 01", "lesson A");

        assertThat(inputDto.getTranslations().get(1).getId()).isEqualTo(2);
        assertThat(inputDto.getTranslations().get(1).getJapaneseId()).isEqualTo(1);
        assertThat(inputDto.getTranslations().get(1).getLocale()).isEqualTo("fr_FR");
        assertThat(inputDto.getTranslations().get(1).getInput()).isEqualTo("Je, moi");
        assertThat(inputDto.getTranslations().get(1).getSortLetter()).isEqualTo("J");
        assertThat(inputDto.getTranslations().get(1).getDetails()).isNullOrEmpty();
        assertThat(inputDto.getTranslations().get(1).getExample()).isNullOrEmpty();
        assertThat(inputDto.getTranslations().get(1).getTags()).containsExactly("leçon 01", "leçon A");
    }

    @Test
    void getTags() {
        // when
        var tags = lessonDao.getTags();

        // then
        assertThat(tags).hasSize(24);
        assertThat(tags).containsExactlyInAnyOrder(
                "lesson 01",
                "lesson 02",
                "lesson 03",
                "lesson 04",
                "lesson 05",
                "lesson 06",
                "lesson 07",
                "lesson 08",
                "lesson 09",
                "lesson 10",
                "lesson 11",
                "lesson A",
                "leçon 01",
                "leçon 02",
                "leçon 03",
                "leçon 04",
                "leçon 05",
                "leçon 06",
                "leçon 07",
                "leçon 08",
                "leçon 09",
                "leçon 10",
                "leçon 11",
                "leçon A"
        );
    }
}
