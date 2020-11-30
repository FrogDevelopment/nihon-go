package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

import static com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.EntryParser.ENTRY_START;

@Slf4j
@Component
@RequiredArgsConstructor
public class JMDictParser {

    static final String JMDICT_START = "<JMdict>";
    private static final String JMDICT_END = "</JMdict>";

    private final EntryParser entryParser;

    void execute(final Scanner scanner) {
        log.info("- scanning entries");
        try (final var csvWriter = new CsvWriter()) {
            while (scanner.hasNext()) {
                final var line = scanner.nextLine();
                switch (line) {
                    case JMDICT_END:
                        return;

                    case ENTRY_START:
                        csvWriter.call(entryParser.execute(scanner));
                        continue;

                    default:
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
