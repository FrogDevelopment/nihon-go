package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import static fr.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils.getTemporaryFile;
import static fr.frogdevelopment.nihongo.entries.implementation.search.utils.KanaToRomaji.convert;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.deleteIfExists;
import static org.apache.commons.lang3.StringUtils.defaultString;

import fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity.Entry;
import fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity.Sense;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public class CsvWriter implements Closeable {

    private static final String ENTRIES_CSV = "entries.csv";
    private static final String SENSES_CSV = "senses.csv";
    private static final String GLOSSES_CSV = "glosses.csv";

    private final BufferedWriter entriesWriter;
    private final BufferedWriter sensesWriter;
    private final BufferedWriter glossesWriter;

    public static Path getEntries() throws IOException {
        return getTemporaryFile(ENTRIES_CSV);
    }

    public static Path getSenses() throws IOException {
        return getTemporaryFile(SENSES_CSV);
    }

    public static Path getGlosses() throws IOException {
        return getTemporaryFile(GLOSSES_CSV);
    }

    CsvWriter() throws IOException {
        entriesWriter = newWriter(getEntries());
        sensesWriter = newWriter(getSenses());
        glossesWriter = newWriter(getGlosses());
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
                            + convert(entry.getKana()));
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
            var line = senseSeq + "\t"
                       + handleMultipleIsoLang(gloss.getLang()) + "\t"
                       + escapeTrailingBackslash(gloss.getValue());
            glossesWriter.write(line);
            glossesWriter.newLine();
        }
    }

    private static String handleMultipleIsoLang(String lang) {
        switch (lang) {
            case "nld":
                return "dut";
            case "fre":
                return "fra";
            case "deu":
                return "ger";
            default:
                return lang;
        }
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
        glossesWriter.close();
    }
}
