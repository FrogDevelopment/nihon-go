package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import com.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils;

import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.context.scope.Refreshable;
import jakarta.inject.Singleton;

@Slf4j
@Singleton
@Refreshable
public class FetchJMDict {

    private final String url;

    private final AboutParser aboutParser;
    private final JMDictParser jmDictParser;

    public FetchJMDict(@Value("${frog.jmdict.url}") final String url,
                       final AboutParser aboutParser,
                       final JMDictParser jmDictParser) {
        this.url = url;
        this.aboutParser = aboutParser;
        this.jmDictParser = jmDictParser;
    }

    public String execute() throws IOException, URISyntaxException {
        log.info("****** Fetching data");
        final var path = FileUtils.downloadFromUrl(url);

        log.info("****** Parsing data");
        final var date = new StringBuilder();
        FileUtils.scan(path, scanner -> date.append(read(scanner)));

        return date.toString();
    }

    String read(final Scanner scanner) {
        String line;
        String date = null;
        while (scanner.hasNext()) {
            line = scanner.nextLine();

            if (date == null) {
                date = aboutParser.execute(line);
            } else if (JMDictParser.JMDICT_START.equals(line)) {
                jmDictParser.execute(scanner);
            }
        }

        if (date == null) {
            throw new IllegalStateException("No jmdict date found");
        }

        return date;
    }

}
