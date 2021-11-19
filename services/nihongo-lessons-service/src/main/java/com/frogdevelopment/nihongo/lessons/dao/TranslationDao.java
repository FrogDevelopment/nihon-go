package com.frogdevelopment.nihongo.lessons.dao;

import com.frogdevelopment.nihongo.lessons.entity.Translation;

public interface TranslationDao {
    int create(int japaneseId, Translation translation);

    void delete(int translationId);

    void update(Translation translation);
}
