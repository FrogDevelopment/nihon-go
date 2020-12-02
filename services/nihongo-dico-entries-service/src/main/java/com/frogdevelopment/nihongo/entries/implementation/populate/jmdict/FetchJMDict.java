package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict;

import com.frogdevelopment.nihongo.entries.implementation.populate.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

@Slf4j
@Component
@RefreshScope
public class FetchJMDict {

    private final String url;

    private final AboutParser aboutParser;
    private final JMDictParser jmDictParser;

    public FetchJMDict(@Value("${frog.jmdict.url}") String url,
                       AboutParser aboutParser,
                       JMDictParser jmDictParser) {
        this.url = url;
        this.aboutParser = aboutParser;
        this.jmDictParser = jmDictParser;
    }

    public String execute() throws IOException, URISyntaxException {
        log.info("****** Fetching data");
        var path = FileUtils.downloadFromUrl(url);

        log.info("****** Parsing data");
        var date = new StringBuilder();
        FileUtils.scan(path, scanner -> date.append(read(scanner)));

        return date.toString();
    }

    String read(Scanner scanner) {
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
