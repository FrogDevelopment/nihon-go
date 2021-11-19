package com.frogdevelopment.nihongo.lessons.application.manage;

import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.frogdevelopment.nihongo.lessons.entity.InputDto;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class ManageLessonsImplTest {

    @InjectMocks
    private ManageLessonsImpl manageLessons;

    @Mock
    private CreateLesson createLesson;
    @Mock
    private UpdateLesson updateLesson;
    @Mock
    private DeleteLesson deleteLesson;

    private final InputDto inputDto = InputDto.builder().build();

    @Test
    void should_insert() {
        // when
        manageLessons.insert(inputDto);

        // then
        then(createLesson).should().call(inputDto);
    }

    @Test
    void should_update() {
        // when
        manageLessons.update(inputDto);

        // then
        then(updateLesson).should().call(inputDto);
    }

    @Test
    void should_delete() {
        // when
        manageLessons.delete(inputDto);

        // then
        then(deleteLesson).should().call(inputDto);
    }
}