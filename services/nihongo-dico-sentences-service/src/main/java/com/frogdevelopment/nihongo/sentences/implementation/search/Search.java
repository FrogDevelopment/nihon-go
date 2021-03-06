package com.frogdevelopment.nihongo.sentences.implementation.search;

import com.frogdevelopment.nihongo.sentences.implementation.search.entity.Sentence;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class Search {

    private final SearchDao searchDao;

    public Search(final SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    public Collection<Sentence> call(final String lang, final String kanji, final String kana, final String gloss) {
        return searchDao.getSentences(lang, kanji, kana, gloss);
    }

}
