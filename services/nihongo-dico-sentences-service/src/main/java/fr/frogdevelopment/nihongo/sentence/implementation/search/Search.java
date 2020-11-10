package fr.frogdevelopment.nihongo.sentence.implementation.search;

import fr.frogdevelopment.nihongo.sentence.implementation.search.entity.Sentence;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
public class Search {

    private final SearchDao searchDao;

    public Search(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    public Collection<Sentence> call(String lang, String kanji, String kana, String gloss) {
        return searchDao.getSentences(lang, kanji, kana, gloss);
    }

}
