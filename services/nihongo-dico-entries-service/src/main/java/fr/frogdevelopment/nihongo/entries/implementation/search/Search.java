package fr.frogdevelopment.nihongo.entries.implementation.search;

import static fr.frogdevelopment.nihongo.entries.implementation.search.utils.ComputeSimilarity.computeSimilarity;
import static fr.frogdevelopment.nihongo.entries.implementation.search.utils.InputUtils.containsKanji;
import static fr.frogdevelopment.nihongo.entries.implementation.search.utils.InputUtils.isOnlyKana;
import static java.lang.Character.isWhitespace;
import static java.util.Comparator.comparingDouble;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import fr.frogdevelopment.nihongo.entries.implementation.search.entity.SearchResult;
import fr.frogdevelopment.nihongo.entries.implementation.search.utils.Input;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Search {

    private static final String REGEX_SEARCH_SPLIT = "\\+|!|\\?";

    private final SearchDao searchDao;

    public Search(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    @Transactional(propagation = REQUIRED)
    public Collection<SearchResult> call(String lang, String query) {
        Input input = getInputType(query);

        return searchDao.search(lang, toFtsQuery(query), input)
                .stream()
                .peek(row -> computeSimilarity(row, query, input))
                .sorted(comparingDouble(SearchResult::getSimilarity).reversed())
                .collect(Collectors.toList());
    }

    private static Input getInputType(String query) {
        Input input;
        if (containsKanji(query)) {
            input = Input.KANJI;
        } else if (isOnlyKana(query)) {
            input = Input.KANA;
        } else {
            input = Input.ROMAJI;
        }
        return input;
    }

    private static String toFtsQuery(final String query) {

        // query by word1+word2+...
        var searches = query.split(REGEX_SEARCH_SPLIT);

        var stringBuilder = new StringBuilder();
        // if other words, check either include (+) or exclude (-) from query
        for (var search : searches) { // start 1, as first word already proceed
            if (isBlank(search)) {
                continue;
            }

            search = search.trim();

            // check the character in front of word to know if inclusion or exclusion
            int startOfWord = query.indexOf(search);

            if (startOfWord > 0) {
                char charAt;
                do {
                    charAt = query.charAt(--startOfWord);
                } while (isWhitespace(charAt));

                switch (charAt) {
                    case '?': // OR
                        stringBuilder.append(" OR ");
                        break;
                    case '!': // NOT
                        stringBuilder.append(" -");
                        break;
                    default:
                        //
                }
            }

            // phrase query or double-word => enclosing in double quotes
            if (search.split("\\s").length > 1 || search.contains("-")) {
                stringBuilder.append("\"").append(cleanWord(search)).append("\"");
            } else {
                stringBuilder.append(cleanWord(search));
            }
        }

        return stringBuilder.toString();
    }

    private static String cleanWord(String word) {
        return word
                .trim() // remove leading and trailing spaces
                .replace("'", "''") // replace ['] by [''] for sql syntax
                ; // todo other ?
    }

}
