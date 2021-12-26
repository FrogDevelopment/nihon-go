package com.frogdevelopment.nihongo.lessons.application;

import com.frogdevelopment.nihongo.lessons.entity.InputDto;

public interface ManageLessons {
    InputDto insert(InputDto inputDto);

    InputDto update(InputDto inputDto);

    void delete(InputDto inputDto);
}
