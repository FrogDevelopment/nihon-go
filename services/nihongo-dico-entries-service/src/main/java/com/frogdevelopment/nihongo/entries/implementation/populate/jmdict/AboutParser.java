package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import jakarta.inject.Singleton;

import static java.util.regex.Pattern.compile;

@Slf4j
@Singleton
class AboutParser {

    private static final Pattern CREATED_PATTERN = compile("^<!-- JMdict created: (?<date>.*) -->$");

    String execute(final String line) {
        log.trace("****** ----------- looking for date");
        final var matcher = CREATED_PATTERN.matcher(line);
        if (matcher.matches()) {
            log.trace("****** ----------- date found");
            return matcher.group("date");
        }

        return null;
    }
}
