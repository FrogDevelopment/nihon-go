package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import com.frogdevelopment.nihongo.entries.implementation.Language;
import com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity.Entry;
import com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity.Sense;
import com.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils;
import com.frogdevelopment.nihongo.entries.implementation.search.utils.KanaToRomaji;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.deleteIfExists;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Slf4j
public class CsvWriter implements AutoCloseable {

    private static final String ENTRIES_CSV = "entries.csv";
    private static final String SENSES_CSV = "senses.csv";
    private static final String GLOSSES_CSV = "glosses_%s.csv";

    private final BufferedWriter entriesWriter;
    private final BufferedWriter sensesWriter;
    private final Map<String, BufferedWriter> glossesWriters = new HashMap<>();

    public static Path getEntries() throws IOException {
        return FileUtils.getTemporaryFile(ENTRIES_CSV);
    }

    public static Path getSenses() throws IOException {
        return FileUtils.getTemporaryFile(SENSES_CSV);
    }

    public static Path getGlosses(String language) {
        try {
            return FileUtils.getTemporaryFile(String.format(GLOSSES_CSV, language));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    CsvWriter() throws IOException {
        entriesWriter = newWriter(getEntries());
        sensesWriter = newWriter(getSenses());
        for (Language language : Language.values()) {
            glossesWriters.put(language.getCode(), newWriter(getGlosses(language.getCode())));
        }

    }

    private BufferedWriter newWriter(Path path) throws IOException {
        deleteIfExists(path);
        return new BufferedWriter(new FileWriter(path.toFile(), UTF_8));
    }

    void call(Entry entry) throws IOException {
        if (entry == null) {
            return;
        }

        entriesWriter.write(entry.getSeq() + "\t"
                + defaultString(entry.getKanji()) + "\t"
                + entry.getKana() + "\t"
                + KanaToRomaji.convert(entry.getKana()));
        entriesWriter.newLine();

        addSenses(entry);
    }

    private void addSenses(Entry entry) throws IOException {
        for (var i = 0; i < entry.getSenses().size(); i++) {
            var sense = entry.getSenses().get(i);
            var senseSeq = entry.getSeq() + "_" + (i + 1);
            var line = senseSeq + "\t"
                    + entry.getSeq() + "\t"
                    + defaultString(join(sense.getPos())) + "\t"
                    + defaultString(join(sense.getField())) + "\t"
                    + defaultString(join(sense.getMisc())) + "\t"
                    + defaultString(sense.getInfo()) + "\t"
                    + defaultString(join(sense.getDial()));
            sensesWriter.write(line);
            sensesWriter.newLine();

            addGlosses(senseSeq, sense);
        }
    }

    private void addGlosses(String senseSeq, Sense sense) throws IOException {
        for (var j = 0; j < sense.getGloss().size(); j++) {
            var gloss = sense.getGloss().get(j);
            var lang = handleMultipleIsoLang(gloss.getLang());
            if (glossesWriters.containsKey(lang)) {
                var line = senseSeq + "\t"
                        + escapeTrailingBackslash(gloss.getValue());
                glossesWriters.get(lang).write(line);
                glossesWriters.get(lang).newLine();
            } else {
                log.warn("Missing Glosses Writer for [{}]", lang);
            }
        }
    }

    private static String handleMultipleIsoLang(String lang) {
        return switch (lang) {
            case "nld" -> "dut";
            case "fre" -> "fra";
            case "deu" -> "ger";
            default -> lang;
        };
    }

    private String escapeTrailingBackslash(String value) {
        if (value.endsWith("\\")) {
            var escaped = new StringBuilder(value);
            escaped.replace(value.lastIndexOf('\\'), value.lastIndexOf('\\') + 1, "\\\\");
            value = escaped.toString();
        }
        return value;
    }

    private static String join(Collection<String> set) {
        return set.isEmpty() ? null : String.join(";", set);
    }

    @Override
    public void close() throws IOException {
        entriesWriter.close();
        sensesWriter.close();
        for (BufferedWriter bufferedWriter : glossesWriters.values()) {
            bufferedWriter.close();
        }
    }
}
