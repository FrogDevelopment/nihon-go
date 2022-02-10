package com.frogdevelopment.nihongo.sentences.implementation.search;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import com.frogdevelopment.nihongo.sentences.implementation.search.entity.Sentence;

import jakarta.inject.Singleton;

@Singleton
@RequiredArgsConstructor
public class Search {

    private final SearchDao searchDao;

    public Collection<Sentence> call(final String lang, final String kanji, final String kana, final String gloss) {
        return searchDao.getSentences(lang, kanji, kana, gloss);
    }

}
