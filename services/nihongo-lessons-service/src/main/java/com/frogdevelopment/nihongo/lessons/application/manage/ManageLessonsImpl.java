package com.frogdevelopment.nihongo.lessons.application.manage;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.application.ManageLessons;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

@Service
@RequiredArgsConstructor
public class ManageLessonsImpl implements ManageLessons {

    private final CreateLesson createLesson;
    private final UpdateLesson updateLesson;
    private final DeleteLesson deleteLesson;

    @Override
    @Transactional(propagation = REQUIRED)
    public InputDto insert(final InputDto inputDto) {
        return createLesson.call(inputDto);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public InputDto update(final InputDto inputDto) {
        return updateLesson.call(inputDto);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public void delete(final InputDto inputDto) {
        deleteLesson.call(inputDto);
    }
}
