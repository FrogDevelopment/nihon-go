package fr.frogdevelopment.nihongo.entries.implementation.search.utils;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @see <a href='https://github.com/nicolas-raoul/jakaroma'>Jakaroma</a>
 * @see <a href='http://www.worddive.com/en/katakana-characters'>http://www.worddive.com/en/katakana-characters</a>
 * @see <a href='http://www.worddive.com/en/hiragana-characters'>http://www.worddive.com/en/hiragana-characters</a>
 * @see <a href='https://en.wikipedia.org/wiki/List_of_Japanese_typographic_symbols'>List of Japanese typographic
 * symbols</a>
 */
@Slf4j
public class KanaToRomaji {

    private static final Map<String, String> KANA = new HashMap<>();
    private static final char HIRAGANA_SOKUON = '\u3063';
    private static final char KATAKANA_SOKUON = '\u30c4';
    /**
     * fixme Indicates a lengthened vowel sound. Often used with katakana
     */
    private static final char KATAKANA_CHOONPU = '\u30fc';

    // static init
    static {

        // SPECIAL CHARACTERS
        // tild
        KANA.put("\u301c", "-");
        // nakaguro => Used to separate foreign words and items in lists
        KANA.put("\u30fb", " ");
        // tōten => comma
        KANA.put("\u3001", ",");
        // kuten => end of sentence
        KANA.put("\u3002", ".");
        // tensen => japanese ellipsis
        KANA.put("\u2026", "…");
        // kagi => quotation mark / open
        KANA.put("\u300C", "“");
        // kagi => quotation mark / close
        KANA.put("\u300D", "”");

        // KATAKANA
        // column A
        KANA.put("\u30a2", "a");
        KANA.put("\u30a4", "i");
        KANA.put("\u30a6", "u");
        KANA.put("\u30a8", "e");
        KANA.put("\u30aa", "o");
        // column KA
        KANA.put("\u30ab", "ka");
        KANA.put("\u30ad", "ki");
        KANA.put("\u30af", "ku");
        KANA.put("\u30b1", "ke");
        KANA.put("\u30b3", "ko");
        // column SA
        KANA.put("\u30b5", "sa");
        KANA.put("\u30b7", "shi");
        KANA.put("\u30b9", "su");
        KANA.put("\u30bb", "se");
        KANA.put("\u30bd", "so");
        // column TA
        KANA.put("\u30bf", "ta");
        KANA.put("\u30c1", "chi");
        KANA.put("\u30c4", "tsu");
        KANA.put("\u30c6", "te");
        KANA.put("\u30c8", "to");
        // column NA
        KANA.put("\u30ca", "na");
        KANA.put("\u30cb", "ni");
        KANA.put("\u30cc", "nu");
        KANA.put("\u30cd", "ne");
        KANA.put("\u30ce", "no");
        // column HA
        KANA.put("\u30cf", "ha");
        KANA.put("\u30d2", "hi");
        KANA.put("\u30d5", "fu");
        KANA.put("\u30d8", "he");
        KANA.put("\u30db", "ho");
        // column MA
        KANA.put("\u30de", "ma");
        KANA.put("\u30df", "mi");
        KANA.put("\u30e0", "mu");
        KANA.put("\u30e1", "me");
        KANA.put("\u30e2", "mo");
        // column YA
        KANA.put("\u30e4", "ya");
        KANA.put("\u30e6", "yu");
        KANA.put("\u30e8", "yo");
        // column RA
        KANA.put("\u30e9", "ra");
        KANA.put("\u30ea", "ri");
        KANA.put("\u30eb", "ru");
        KANA.put("\u30ec", "re");
        KANA.put("\u30ed", "ro");
        // column WA
        KANA.put("\u30ef", "wa");
        KANA.put("\u30f2", "wo");
        // column N
        KANA.put("\u30f3", "n");
        // column GA
        KANA.put("\u30ac", "ga");
        KANA.put("\u30ae", "gi");
        KANA.put("\u30b0", "gu");
        KANA.put("\u30b2", "ge");
        KANA.put("\u30b4", "go");
        // column ZA
        KANA.put("\u30b6", "za");
        KANA.put("\u30b8", "ji");
        KANA.put("\u30ba", "zu");
        KANA.put("\u30bc", "ze");
        KANA.put("\u30be", "zo");
        // column DA
        KANA.put("\u30c0", "da");
        KANA.put("\u30c2", "ji");
        KANA.put("\u30c5", "zu");
        KANA.put("\u30c7", "de");
        KANA.put("\u30c9", "do");
        // column BA
        KANA.put("\u30d0", "ba");
        KANA.put("\u30d3", "bi");
        KANA.put("\u30d6", "bu");
        KANA.put("\u30d9", "be");
        KANA.put("\u30dc", "bo");
        // column PA
        KANA.put("\u30d1", "pa");
        KANA.put("\u30d4", "pi");
        KANA.put("\u30d7", "pu");
        KANA.put("\u30da", "pe");
        KANA.put("\u30dd", "po");
        // column KYA
        KANA.put("\u30ad\u30e3", "kya");
        KANA.put("\u30ad\u30e5", "kyu");
        KANA.put("\u30ad\u30e7", "kyo");
        // column SHA　
        KANA.put("\u30b7\u30e3", "sha");
        KANA.put("\u30b7\u30e5", "shu");
        KANA.put("\u30b7\u30e7", "sho");
        // column CHA　
        KANA.put("\u30c1\u30e3", "cha");
        KANA.put("\u30c1\u30e5", "chu");
        KANA.put("\u30c1\u30e7", "cho");
        // column NYA
        KANA.put("\u30cb\u30e3", "nya");
        KANA.put("\u30cb\u30e5", "nyu");
        KANA.put("\u30cb\u30e7", "nyo");
        // column MYA
        KANA.put("\u30df\u30e3", "mya");
        KANA.put("\u30df\u30e5", "myu");
        KANA.put("\u30df\u30e7", "myo");
        // column HYA
        KANA.put("\u30d2\u30e3", "hya");
        KANA.put("\u30d2\u30e5", "hyu");
        KANA.put("\u30d2\u30e7", "hyo");
        // column RYA
        KANA.put("\u30ea\u30e3", "rya");
        KANA.put("\u30ea\u30e5", "ryu");
        KANA.put("\u30ea\u30e7", "ryo");
        // column GYA
        KANA.put("\u30ae\u30e3", "gya");
        KANA.put("\u30ae\u30e5", "gyu");
        KANA.put("\u30ae\u30e7", "gyo");
        // column JA
        KANA.put("\u30b8\u30e3", "ja");
        KANA.put("\u30b8\u30e5", "ju");
        KANA.put("\u30b8\u30e7", "jo");
        // column
        KANA.put("\u30c6\u30a3", "ti");
        KANA.put("\u30d5\u30a3", "fi");
        KANA.put("\u30c7\u30a3", "di");
        // column
        KANA.put("\u30c4\u30a1", "tsa");
        KANA.put("\u30c4\u30a3", "tsi");
        // column　DYA
        KANA.put("\u30c2\u30e3", "dya");
        KANA.put("\u30c2\u30e5", "dyu");
        KANA.put("\u30c2\u30e7", "dyo");
        // column BYA
        KANA.put("\u30d3\u30e3", "bya");
        KANA.put("\u30d3\u30e5", "byu");
        KANA.put("\u30d3\u30e7", "byo");
        // column PYA
        KANA.put("\u30d4\u30e3", "pya");
        KANA.put("\u30d4\u30e5", "pyu");
        KANA.put("\u30d4\u30e7", "pyo");

        KANA.put("\u30d5\u30a1", "fa");
        KANA.put("\u30a6\u30a3", "wi");
        KANA.put("\u30c8\u30a5", "tu");
        KANA.put("\u30c9\u30a5", "du");
        KANA.put("\u30c7\u30a5", "dyu");
        KANA.put("\u30a6\u30a7", "we");
        KANA.put("\u30b7\u30a7", "she");
        KANA.put("\u30c1\u30a7", "che");
        KANA.put("\u30c4\u30a7", "tse");
        KANA.put("\u30d5\u30a7", "fe");
        KANA.put("\u30b8\u30a7", "je");
        KANA.put("\u30a6\u30a9", "wo");
        KANA.put("\u30c4\u30a9", "tso");
        KANA.put("\u30d5\u30a9", "fo");

        // HIRAGANA

        // column A
        KANA.put("\u3042", "a");
        KANA.put("\u3044", "i");
        KANA.put("\u3046", "u");
        KANA.put("\u3048", "e");
        KANA.put("\u304a", "o");
        // column KA
        KANA.put("\u304b", "ka");
        KANA.put("\u304d", "ki");
        KANA.put("\u304f", "ku");
        KANA.put("\u3051", "ke");
        KANA.put("\u3053", "ko");
        // column SA
        KANA.put("\u3055", "sa");
        KANA.put("\u3057", "shi");
        KANA.put("\u3059", "su");
        KANA.put("\u305b", "se");
        KANA.put("\u305d", "so");
        // column TA
        KANA.put("\u305f", "ta");
        KANA.put("\u3061", "chi");
        KANA.put("\u3064", "tsu");
        KANA.put("\u3066", "te");
        KANA.put("\u3068", "to");
        // column NA
        KANA.put("\u306a", "na");
        KANA.put("\u306b", "ni");
        KANA.put("\u306c", "nu");
        KANA.put("\u306d", "ne");
        KANA.put("\u306e", "no");
        // column HA
        KANA.put("\u306f", "ha");
        KANA.put("\u3072", "hi");
        KANA.put("\u3075", "fu");
        KANA.put("\u3078", "he");
        KANA.put("\u307b", "ho");
        // column MA
        KANA.put("\u307e", "ma");
        KANA.put("\u307f", "mi");
        KANA.put("\u3080", "mu");
        KANA.put("\u3081", "me");
        KANA.put("\u3082", "mo");
        // column YA
        KANA.put("\u3084", "ya");
        KANA.put("\u3086", "yu");
        KANA.put("\u3088", "yo");
        // column RA
        KANA.put("\u3089", "ra");
        KANA.put("\u308a", "ri");
        KANA.put("\u308b", "ru");
        KANA.put("\u308c", "re");
        KANA.put("\u308d", "ro");
        // column WA
        KANA.put("\u308f", "wa");
        KANA.put("\u3092", "wo");
        // column N
        KANA.put("\u3093", "n");
        // column GA
        KANA.put("\u304c", "ga");
        KANA.put("\u304e", "gi");
        KANA.put("\u3050", "gu");
        KANA.put("\u3052", "ge");
        KANA.put("\u3054", "go");
        // column ZA
        KANA.put("\u3056", "za");
        KANA.put("\u3058", "ji");
        KANA.put("\u305a", "zu");
        KANA.put("\u305c", "ze");
        KANA.put("\u305e", "zo");
        // column DA
        KANA.put("\u3060", "da");
        KANA.put("\u3062", "ji");
        KANA.put("\u3065", "zu");
        KANA.put("\u3067", "de");
        KANA.put("\u3069", "do");
        // column BA
        KANA.put("\u3070", "ba");
        KANA.put("\u3073", "bi");
        KANA.put("\u3076", "bu");
        KANA.put("\u3079", "be");
        KANA.put("\u307c", "bo");
        // column PA
        KANA.put("\u3071", "pa");
        KANA.put("\u3074", "pi");
        KANA.put("\u3077", "pu");
        KANA.put("\u307a", "pe");
        KANA.put("\u307d", "po");
        // column KYA
        KANA.put("\u304d\u3083", "kya");
        KANA.put("\u304d\u3085", "kyu");
        KANA.put("\u304d\u3087", "kyo");
        // column SHA
        KANA.put("\u3057\u3083", "sha");
        KANA.put("\u3057\u3085", "shu");
        KANA.put("\u3057\u3087", "sho");
        // column CHA
        KANA.put("\u3061\u3083", "cha");
        KANA.put("\u3061\u3085", "chu");
        KANA.put("\u3061\u3087", "cho");
        // column NYA
        KANA.put("\u306b\u3083", "nya");
        KANA.put("\u306b\u3085", "nyu");
        KANA.put("\u306b\u3087", "nyo");
        // column NYA
        KANA.put("\u307f\u3083", "nya");
        KANA.put("\u307f\u3085", "nyu");
        KANA.put("\u307f\u3087", "nyo");
        // column HYA
        KANA.put("\u3072\u3083", "hya");
        KANA.put("\u3072\u3085", "hyu");
        KANA.put("\u3072\u3087", "hyo");
        // column RYA
        KANA.put("\u308a\u3083", "rya");
        KANA.put("\u308a\u3085", "ryu");
        KANA.put("\u308a\u3087", "ryo");
        // column GYA
        KANA.put("\u304e\u3083", "gya");
        KANA.put("\u304e\u3085", "gyu");
        KANA.put("\u304e\u3087", "gyo");
        // column JA
        KANA.put("\u3058\u3083", "ja");
        KANA.put("\u3058\u3085", "ju");
        KANA.put("\u3058\u3087", "jo");
        // column BYA
        KANA.put("\u3073\u3083", "bya");
        KANA.put("\u3073\u3085", "byu");
        KANA.put("\u3073\u3087", "byo");
        // column PYA
        KANA.put("\u3074\u3083", "pya");
        KANA.put("\u3074\u3085", "pyu");
        KANA.put("\u3074\u3087", "pyo");
    }

    public static String convert(String value) {
        StringBuilder romaji = new StringBuilder();

        int length = value.length();
        int minus1Character = length - 2;

        String substring;
        for (int i = 0; i < length; i++) {
            char charAt = value.charAt(i);
            if (i <= minus1Character) {
                // KYA, SHA, NYA ...
                substring = value.substring(i, i + 2);
                if (KANA.containsKey(substring)) {
                    romaji.append(KANA.get(substring));
                    // increment 1 more as we take 2 characters
                    i++;
                } else {
                    // A, KA, SA, TA ...
                    substring = value.substring(i, i + 1);
                    if (KANA.containsKey(substring)) {
                        romaji.append(KANA.get(substring));
                    } else {
                        // SOKUON (LITTLE TSU)
                        if ((charAt == HIRAGANA_SOKUON) || (charAt == KATAKANA_SOKUON)) {
                            // skip little tsu and take next character
                            substring = value.substring(i + 1, i + 2);
                            String kana = KANA.get(substring);
                            if (kana == null) { // case with 2 sokuon (i.e.: ぶほっっ)
                                log.warn("skip character position {} from {}", i, value);
                                continue;
                            }
                            // add only consonant
                            romaji.append(kana.charAt(0));
                        } else {
                            // UNKNOWN
                            romaji.append(charAt);
                        }
                    }
                }
            } else { // last character
                // A, KA, SA, TA ...
                substring = value.substring(i, i + 1);
                if (KANA.containsKey(substring)) {
                    romaji.append(KANA.get(substring));
                } else {
                    // UNKNOWN
                    romaji.append(charAt);
                }
            }
        }

        return romaji.toString();
    }

}
