package com.frogdevelopment.nihongo.lessons.application.lesson;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.frogdevelopment.nihongo.lessons.dao.InputDao;

import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonServiceImpl lessonServiceImpl;

    @Mock
    private InputDao inputDao;

    @Test
    void fetch_should_manage_wrong_sortOrder() {
        // given
        var lesson = 1;
        var sortField = "sortField";
        var sortOrder = "desccend";

        // when
        lessonServiceImpl.fetch(lesson, sortField, sortOrder);

        // then
        then(inputDao)
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
        then(inputDao)
                .should()
                .fetch(lesson, sortField, "desc");
    }

}
