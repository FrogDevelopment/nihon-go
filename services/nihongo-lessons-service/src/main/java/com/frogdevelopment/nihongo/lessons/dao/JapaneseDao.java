package com.frogdevelopment.nihongo.lessons.dao;

import com.frogdevelopment.nihongo.lessons.entity.Japanese;

public interface JapaneseDao {
    int create(Japanese japanese);

    void update(Japanese japanese);

    void delete(Japanese japanese);
}
