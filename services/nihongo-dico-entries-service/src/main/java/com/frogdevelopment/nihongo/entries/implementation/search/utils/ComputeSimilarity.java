package com.frogdevelopment.nihongo.entries.implementation.search.utils;

import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchResult;
import lombok.NoArgsConstructor;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ComputeSimilarity {

    public static void computeSimilarity(SearchResult row,
                                         final String query,
                                         final Input input) {
        // keep max similarity
        row.setSimilarity(0);

        String value;
        switch (input) {
            case KANJI:
            case KANA:

                if (input == Input.KANJI) {
                    value = row.getKanji();
                } else {
                    value = row.getKana();
                }

                // compute similarity
                row.setSimilarity(computeSimilarity(query, value));

                // if only 1 word and match => has to be first multiple values => fixme ???? don't remember why ô0 !!
                if (row.getSimilarity() == 1) {
                    row.setSimilarity(2);
                }

                break;

            case ROMAJI:
            default:
                value = row.getVocabulary();

                double similarity = computeSimilarity(query, value);

                if (similarity > row.getSimilarity()) {
                    row.setSimilarity(similarity);
                }
        }
    }

    // http://rosettacode.org/wiki/Levenshtein_distance#Java
    private static double computeSimilarity(String s1, String s2) {
        s1 = new String(s1.getBytes(UTF_8));
        s2 = new String(s2.getBytes(UTF_8));

        if (s1.length() < s2.length()) { // s1 should always be bigger
            var swap = s1;
            s1 = s2;
            s2 = swap;
        }
        var bigLen = s1.length();
        if (bigLen == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (bigLen - computeEditDistance(s1, s2)) / (double) bigLen;
    }

    // http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    private static int computeEditDistance(String s0, String s1) {

        var len0 = s0.length() + 1;
        var len1 = s1.length() + 1;

        // the array of distances
        var oldCost = new int[len0]; // j -2
        var cost = new int[len0]; // j-1
        var newCost = new int[len0]; // j

        // initial cost of skipping prefix in String s0
        for (var i = 0; i < len0; i++) {
            cost[i] = i;
        }

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (var j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newCost[0] = j - 1;

            // transformation cost for each letter in s0
            for (var i = 1; i < len0; i++) {

                // matching current letters in both strings
                var match = s0.charAt(i - 1) == s1.charAt(j - 1) ? 0 : 1;

                // computing cost for each transformation
                var costDelete = newCost[i - 1] + 1; // deletion
                var costInsert = cost[i] + 1; // insertion
                var costReplace = cost[i - 1] + match; // substitution

                // keep minimum cost
                newCost[i] = Math.min(Math.min(costInsert, costDelete), costReplace);

                // http://fr.wikipedia.org/wiki/Distance_de_Damerau-Levenshtein
                if (i > 1 && j > 1 && s0.charAt(i - 1) == s1.charAt(j - 2) && s0.charAt(i - 1) == s1.charAt(j - 1)) {
                    var costTransposition = oldCost[i - 2] + match; // transposition

                    newCost[i] = Math.min(newCost[i], costTransposition);
                }
            }

            // swap cost/new cost arrays
            oldCost = cost;
            var swap = cost;
            cost = newCost;
            newCost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
}
