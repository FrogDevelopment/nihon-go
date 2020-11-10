package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import static java.util.regex.Pattern.compile;

import java.util.Scanner;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class KanjiParser {

    static final String KANJI_ELEMENT_START = "<k_ele>";
    private static final String KANJI_ELEMENT_END = "</k_ele>";

    private static final Pattern KANJI_PATTERN = compile("<keb>(?<kanji>.*)</keb>");

    String execute(Scanner scanner) {
        log.trace("****** ----------- looking for Kanji");
        String kanji = null;
        while (scanner.hasNext()) {
            var line = scanner.nextLine();

            if (KANJI_ELEMENT_END.equals(line)) {
                break;
            } else {
                var matcher = KANJI_PATTERN.matcher(line);
                if (matcher.matches()) {
                    log.trace("****** ----------- Kanji found");
                    kanji = matcher.group("kanji");
                }
            }
        }

        return kanji;
    }

}
