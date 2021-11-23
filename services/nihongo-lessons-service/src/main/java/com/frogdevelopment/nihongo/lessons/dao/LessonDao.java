package com.frogdevelopment.nihongo.lessons.dao;

import java.util.List;

import com.frogdevelopment.nihongo.lessons.entity.InputDto;

public interface LessonDao {

    Integer getTotal();

    List<InputDto> fetch(int pageIndex, int pageSize, String sortField, String sortOrder);
}
