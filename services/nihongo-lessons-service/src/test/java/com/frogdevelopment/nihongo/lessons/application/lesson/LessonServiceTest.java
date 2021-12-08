package com.frogdevelopment.nihongo.lessons.application.lesson;

import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonServiceImpl lessonServiceImpl;

    @Mock
    private LessonDao lessonDao;

    @Test
    void fetch_should_manage_wrong_sortOrder() {
        // given
        var lesson = 1;
        var sortField = "sortField";
        var sortOrder = "desccend";

        // when
        lessonServiceImpl.fetch(lesson, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(lesson, sortField, "asc");
    }

    @Test
    void fetch_should_manage_right_sortOrder() {
        // given
        var lesson = 1;
        var sortField = "sortField";
        var sortOrder = "descend";

        // when
        lessonServiceImpl.fetch(lesson, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(lesson, sortField, "desc");
    }

}
