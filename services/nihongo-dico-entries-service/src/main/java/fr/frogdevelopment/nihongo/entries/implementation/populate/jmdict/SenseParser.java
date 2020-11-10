package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;

import fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity.Gloss;
import fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity.Sense;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class SenseParser {

    static final String SENSE_START = "<sense>";
    private static final String SENSE_END = "</sense>";

    private static final Pattern POS_PATTERN = compile("^<pos>&(?<pos>.+);</pos>$");
    private static final Pattern XREF_PATTERN = compile("^<xref>(?<reading>.+)</xref>$");
    private static final Pattern ANT_PATTERN = compile("^<ant>(?<reading>.+)</ant>$");
    private static final Pattern FIELD_PATTERN = compile("^<field>&(?<field>.+);</field>$");
    private static final Pattern MISC_PATTERN = compile("^<misc>&(?<misc>.+);</misc>$");
    private static final Pattern INFO_PATTERN = compile("^<s_inf>(?<info>.+)</s_inf>$");
    private static final Pattern DIAL_PATTERN = compile("^<dial>&(?<dial>.+);</dial>$");
    private static final Pattern GLOSS_PATTERN = compile(
            "^<gloss( xml:lang=\"(?<lang>\\w{3})\")?>(?<value>.+)</gloss>$");

    private static Gloss readGloss(Matcher matcher) {
        var lang = matcher.group("lang");
        if (isBlank(lang)) {
            lang = "eng";
        }

        var value = matcher.group("value");
        if (isBlank(value)) {
            return null;
        }
        return Gloss.builder()
                .lang(lang)
                .value(value)
                .build();
    }

    Sense execute(Scanner scanner) {
        log.trace("****** ----------- parsing Sense");
        Matcher matcher;
        var senseBuilder = Sense.builder();
        while (scanner.hasNext()) {
            var line = scanner.nextLine();

            if (SENSE_END.equals(line)) {
                return senseBuilder.build();
            } else {
                matcher = XREF_PATTERN.matcher(line);
                if (matcher.matches()) {
                    // todo manage cross-reference
                    continue;
                }

                matcher = ANT_PATTERN.matcher(line);
                if (matcher.matches()) {
                    // todo manage antonym
                    continue;
                }

                matcher = POS_PATTERN.matcher(line);
                if (matcher.matches()) {
                    senseBuilder.pos(matcher.group("pos"));
                    continue;
                }

                matcher = FIELD_PATTERN.matcher(line);
                if (matcher.matches()) {
                    senseBuilder.field(matcher.group("field"));
                    continue;
                }

                matcher = MISC_PATTERN.matcher(line);
                if (matcher.matches()) {
                    senseBuilder.misc(matcher.group("misc"));
                    continue;
                }

                matcher = INFO_PATTERN.matcher(line);
                if (matcher.matches()) {
                    senseBuilder.info(matcher.group("info"));
                    continue;
                }

                matcher = DIAL_PATTERN.matcher(line);
                if (matcher.matches()) {
                    senseBuilder.dial(matcher.group("dial"));
                    continue;
                }

                matcher = GLOSS_PATTERN.matcher(line);
                if (matcher.matches()) {
                    var gloss = readGloss(matcher);
                    if (gloss != null) {
                        senseBuilder.gloss(gloss);
                    }
                }
            }
        }

        return null;
    }

}
