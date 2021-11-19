package com.frogdevelopment.nihongo.ftp;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;

public class LoggingOutputStream extends OutputStream {

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
    private final Logger logger;
    private final LogLevel level;

    public LoggingOutputStream(final Logger logger, final LogLevel level) {
        this.logger = logger;
        this.level = level;
    }

    public static void redirectSysOutAndSysErr(final Logger logger) {
        System.setOut(new PrintStream(new LoggingOutputStream(logger, LogLevel.INFO)));
        System.setErr(new PrintStream(new LoggingOutputStream(logger, LogLevel.ERROR)));
    }

    @Override
    public void write(final int b) {
        if (b == '\n') {
            final String line = byteArrayOutputStream.toString();
            byteArrayOutputStream.reset();

            switch (level) {
                case TRACE -> logger.trace(line);
                case DEBUG -> logger.debug(line);
                case ERROR -> logger.error(line);
                case INFO -> logger.info(line);
                case WARN -> logger.warn(line);
            }
        } else {
            byteArrayOutputStream.write(b);
        }
    }

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR,
    }

}
