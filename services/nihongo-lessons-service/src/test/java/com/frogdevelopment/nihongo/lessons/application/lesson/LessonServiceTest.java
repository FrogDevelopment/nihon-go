package com.frogdevelopment.nihongo.lessons.application.lesson;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.frogdevelopment.nihongo.lessons.dao.LessonDao;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonServiceImpl lessonServiceImpl;

    @Mock
    private LessonDao lessonDao;

    @Test
    void fetch_should_call_dao() {
        // when
        lessonServiceImpl.getTotal();

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
        lessonServiceImpl.fetch(pageIndex, pageSize, sortField, sortOrder);

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
        lessonServiceImpl.fetch(pageIndex, pageSize, sortField, sortOrder);

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
        lessonServiceImpl.fetch(pageIndex, pageSize, sortField, sortOrder);

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
        lessonServiceImpl.fetch(pageIndex, pageSize, sortField, sortOrder);

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
        lessonServiceImpl.fetch(pageIndex, pageSize, sortField, sortOrder);

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
        lessonServiceImpl.fetch(pageIndex, pageSize, sortField, sortOrder);

        // then
        then(lessonDao)
                .should()
                .fetch(pageIndex, pageSize, sortField, "desc");
    }

    @Test
    void getTags_should_call_dao() {
        // when
        lessonServiceImpl.getTags();

        // then
        then(lessonDao)
                .should()
                .getTags();
    }

}
