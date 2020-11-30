package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@Slf4j
@Component
class ReadingParser {

    static final String READING_ELEMENT_START = "<r_ele>";
    private static final String READING_ELEMENT_END = "</r_ele>";
    private static final Pattern READING_PATTERN = compile("<reb>(?<reading>.*)</reb>");

    String execute(final Scanner scanner) {
        log.trace("****** ----------- looking for Reading");
        String reading = null;
        while (scanner.hasNext()) {
            final var line = scanner.nextLine();

            if (READING_ELEMENT_END.equals(line)) {
                return reading;
            } else {
                final var matcher = READING_PATTERN.matcher(line);
                if (matcher.matches()) {
                    log.trace("****** ----------- Reading found");
                    reading = matcher.group("reading");
                }
            }
        }
        return reading;
    }
}
