package com.frogdevelopment.nihongo.lessons.application.manage;

import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import com.frogdevelopment.nihongo.lessons.application.ManageLessons;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import jakarta.inject.Singleton;

import static javax.transaction.Transactional.TxType.REQUIRED;

@Singleton
@RequiredArgsConstructor
public class ManageLessonsImpl implements ManageLessons {

    private final CreateLesson createLesson;
    private final UpdateLesson updateLesson;
    private final DeleteLesson deleteLesson;

    @Override
    @Transactional(REQUIRED)
    public InputDto insert(final InputDto inputDto) {
        return createLesson.call(inputDto);
    }

    @Override
    @Transactional(REQUIRED)
    public InputDto update(final InputDto inputDto) {
        return updateLesson.call(inputDto);
    }

    @Override
    @Transactional(REQUIRED)
    public void delete(final InputDto inputDto) {
        deleteLesson.call(inputDto);
    }
}
