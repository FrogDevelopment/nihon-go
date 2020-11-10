package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import static fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.JMDictParser.JMDICT_START;
import static fr.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils.downloadFromUrl;
import static fr.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils.scan;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RefreshScope
public class FetchJMDict {

    private final String url;

    private final AboutParser aboutParser;
    private final JMDictParser jmDictParser;

    @Autowired
    public FetchJMDict(@Value("${frog.jmdict.url}") String url,
                       AboutParser aboutParser,
                       JMDictParser jmDictParser) {
        this.url = url;
        this.aboutParser = aboutParser;
        this.jmDictParser = jmDictParser;
    }

    public String execute() throws IOException, URISyntaxException {
        log.info("****** Fetching data");
        var path = downloadFromUrl(url);

        log.info("****** Parsing data");
        var date = new StringBuilder();
        scan(path, scanner -> date.append(read(scanner)));

        return date.toString();
    }

    String read(Scanner scanner) {
        String line;
        String date = null;
        while (scanner.hasNext()) {
            line = scanner.nextLine();

            if (date == null) {
                date = aboutParser.execute(line);
            } else if (JMDICT_START.equals(line)) {
                jmDictParser.execute(scanner);
            }
        }

        if (date == null) {
            throw new IllegalStateException("No jmdict date found");
        }

        return date;
    }

}
