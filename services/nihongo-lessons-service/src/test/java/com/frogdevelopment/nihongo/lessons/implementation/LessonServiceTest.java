package com.frogdevelopment.nihongo.lessons.implementation;

import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonDao lessonDao;

    @Test
    void getLesson() {
        // when
        lessonService.getLesson("fr_FR", "01");

        // then
        then(lessonDao)
                .should()
                .getLesson("fr_FR", "01");
    }

    @Test
    void fetch_should_call_dao() {
        // when
        lessonService.getTotal();

        // then
        then(lessonDao)
                .should()
                .getTotal();
    }

    @Test
    void fetch_should_manage_null_sortField() {
        // given
        var pageIndex = 6;
        var pageSize = 453;
        String sortField = null;
        var sortOrder = "sortOrder";

        // when
        lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(pageIndex, pageSize, "japanese_id", "asc");
    }

    @Test
    void fetch_should_manage_empty_sortField() {
        // given
        var pageIndex = 6;
        var pageSize = 453;
        var sortField = "";
        var sortOrder = "sortOrder";

        // when
        lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(pageIndex, pageSize, "japanese_id", "asc");
    }

    @Test
    void fetch_should_manage_null_sortOrder() {
        // given
        var pageIndex = 6;
        var pageSize = 453;
        var sortField = "sortField";
        String sortOrder = null;

        // when
        lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(pageIndex, pageSize, sortField, "asc");
    }

    @Test
    void fetch_should_manage_empty_sortOrder() {
        // given
        var pageIndex = 6;
        var pageSize = 453;
        var sortField = "sortField";
        var sortOrder = "";

        // when
        lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        verify(lessonDao).fetch(pageIndex, pageSize, sortField, "asc");
    }

    @Test
    void fetch_should_manage_wrong_sortOrder() {
        // given
        var pageIndex = 6;
        var pageSize = 453;
        var sortField = "sortField";
        var sortOrder = "desccend";

        // when
        lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(pageIndex, pageSize, sortField, "asc");
    }

    @Test
    void fetch_should_manage_right_sortOrder() {
        // given
        var pageIndex = 6;
        var pageSize = 453;
        var sortField = "sortField";
        var sortOrder = "descend";

        // when
        lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(pageIndex, pageSize, sortField, "desc");
    }

    @Test
    void getTags_should_call_dao() {
        // when
        lessonService.getTags();

        // then
        then(lessonDao)
                .should()
                .getTags();
    }

}
