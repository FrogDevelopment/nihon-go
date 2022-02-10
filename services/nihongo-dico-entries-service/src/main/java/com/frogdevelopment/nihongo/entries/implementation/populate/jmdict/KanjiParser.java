package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.regex.Pattern;

import jakarta.inject.Singleton;

import static java.util.regex.Pattern.compile;

@Slf4j
@Singleton
class KanjiParser {

    static final String KANJI_ELEMENT_START = "<k_ele>";
    private static final String KANJI_ELEMENT_END = "</k_ele>";

    private static final Pattern KANJI_PATTERN = compile("<keb>(?<kanji>.*)</keb>");

    String execute(final Scanner scanner) {
        log.trace("****** ----------- looking for Kanji");
        String kanji = null;
        while (scanner.hasNext()) {
            final var line = scanner.nextLine();

            if (KANJI_ELEMENT_END.equals(line)) {
                break;
            } else {
                final var matcher = KANJI_PATTERN.matcher(line);
                if (matcher.matches()) {
                    log.trace("****** ----------- Kanji found");
                    kanji = matcher.group("kanji");
                }
            }
        }

        return kanji;
    }

}
