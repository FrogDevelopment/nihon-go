package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class AboutParser {

    private static final Pattern CREATED_PATTERN = compile("^<!-- JMdict created: (?<date>.*) -->$");

    String execute(String line) {
        log.trace("****** ----------- looking for date");
        var matcher = CREATED_PATTERN.matcher(line);
        if (matcher.matches()) {
            log.trace("****** ----------- date found");
            return matcher.group("date");
        }

        return null;
    }
}