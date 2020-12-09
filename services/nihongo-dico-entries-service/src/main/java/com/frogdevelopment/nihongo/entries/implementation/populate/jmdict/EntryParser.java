package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
class EntryParser {

    static final String ENTRY_START = "<entry>";
    private static final String ENTRY_END = "</entry>";
    private static final Pattern SEQ_PATTERN = compile("^<ent_seq>(?<seq>\\d+)</ent_seq>$");

    private final KanjiParser kanjiParser;
    private final ReadingParser readingParser;
    private final SenseParser senseParser;

    Entry execute(Scanner scanner) {
        var entryBuilder = Entry.builder();

        while (scanner.hasNext()) {
            var line = scanner.nextLine();

            switch (line) {
                case ENTRY_END:
                    return addEntry(entryBuilder);

                case KanjiParser.KANJI_ELEMENT_START:
                    var kanji = kanjiParser.execute(scanner);
                    if (isNotBlank(kanji)) {
                        entryBuilder.kanji(kanji);
                    }
                    continue;

                case ReadingParser.READING_ELEMENT_START:
                    var reading = readingParser.execute(scanner);
                    if (isNotBlank(reading)) {
                        entryBuilder.kana(reading);
                    }
                    continue;

                case SenseParser.SENSE_START:
                    var sense = senseParser.execute(scanner);
                    if (sense != null) {
                        entryBuilder.sense(sense);
                    }
                    continue;

                default:
                    var matcher = SEQ_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        entryBuilder.seq(matcher.group("seq"));
                    }
            }
        }
        return null;
    }

    private Entry addEntry(Entry.EntryBuilder entryBuilder) {
        var entry = entryBuilder.build();

        if (entry.getSeq() != null) {
            // do not add entry if empty senses
            if (entry.getSenses().isEmpty()) {
                log.warn("entry {} without senses", entry.getSeq());
                return null;
            } else {
                applyPosToAllSenses(entry);
                applyFieldToAllSenses(entry);
                applyMiscToAllSenses(entry);
                applyInfoToAllSenses(entry);
                applyDialToAllSenses(entry);
                return entry;
            }
        } else {
            log.warn("entry {} without seq", entry);
            return null;
        }
    }

    private void applyPosToAllSenses(Entry entry) {
        entry.getSenses()
                .stream()
                .filter(s -> !isEmpty(s.getPos()))
                .findFirst()
                .ifPresent(sense -> {
                    var pos = sense.getPos();

                    entry.getSenses()
                            .stream()
                            .filter(s -> isEmpty(s.getPos()))
                            .forEach(s -> s.setPos(pos));
                });
    }

    private void applyFieldToAllSenses(Entry entry) {
        entry.getSenses()
                .stream()
                .filter(s -> !isEmpty(s.getField()))
                .findFirst()
                .ifPresent(sense -> {
                    var field = sense.getField();

                    entry.getSenses()
                            .stream()
                            .filter(s -> isEmpty(s.getField()))
                            .forEach(s -> s.setField(field));
                });
    }

    private void applyMiscToAllSenses(Entry entry) {
        entry.getSenses()
                .stream()
                .filter(s -> !isEmpty(s.getMisc()))
                .findFirst()
                .ifPresent(sense -> {
                    var misc = sense.getMisc();

                    entry.getSenses()
                            .stream()
                            .filter(s -> isEmpty(s.getMisc()))
                            .forEach(s -> s.setMisc(misc));
                });
    }

    private void applyInfoToAllSenses(Entry entry) {
        entry.getSenses()
                .stream()
                .filter(s -> isNotBlank(s.getInfo()))
                .findFirst()
                .ifPresent(sense -> {
                    var info = sense.getInfo();

                    entry.getSenses()
                            .stream()
                            .filter(s -> isBlank(s.getInfo()))
                            .forEach(s -> s.setInfo(info));
                });
    }

    private void applyDialToAllSenses(Entry entry) {
        entry.getSenses()
                .stream()
                .filter(s -> !s.getDial().isEmpty())
                .findFirst()
                .ifPresent(sense -> {
                    var dial = sense.getDial();

                    entry.getSenses()
                            .stream()
                            .filter(s -> s.getDial().isEmpty())
                            .forEach(s -> s.setDial(dial));
                });
    }

}
