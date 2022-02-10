package com.frogdevelopment.nihongo.ftp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.PreDestroy;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import static com.frogdevelopment.nihongo.ftp.LoggingOutputStream.LogLevel.DEBUG;

@Slf4j
@RequiredArgsConstructor
public class FtpClientHelper implements Closeable {

    private final FtpProperties ftpProperties;
    private final FTPClient ftpClient;

    public void open() throws IOException {
        LoggingOutputStream.redirectToLogger(ftpClient, log, DEBUG);

        ftpClient.connect(ftpProperties.getServer(), ftpProperties.getPort());
        final int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("Exception when connecting to FTP Server");
        }

        final var loginSuccess = ftpClient.login(ftpProperties.getUser(), ftpProperties.getPassword());
        if (!loginSuccess) {
            throw new IllegalArgumentException("Incorrect credentials");
        }
    }

    @Override
    @PreDestroy
    public void close() throws IOException {
        if (ftpClient.isConnected()) {
            log.debug("Closing FTP client");
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public void downloadFile(final String source, final String destination) throws IOException {
        try (final var local = new FileOutputStream(destination)) {
            final var success = ftpClient.retrieveFile(source, local);
            if (!success) {
                throw new IllegalStateException("Failed to download file from " + source);
            }
        }
    }

    public InputStream retrieveFileStream(final String source) throws IOException {
        return ftpClient.retrieveFileStream(source);
    }

    public void storeFile(final String path, final File file) throws IOException {
        try (final var inputStream = new FileInputStream(file)) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
            ftpClient.setCharset(StandardCharsets.UTF_8);
            ftpClient.enterLocalPassiveMode();
            final var fileStored = ftpClient.storeFile(path, inputStream);
            if (fileStored) {
                log.debug("Successfully stored file {} into {}", file, path);
            } else {
                log.error("Unable to store file {} into {}", file, path);
            }
        }
    }

    public Collection<String> listFiles(final String path) throws IOException {
        final FTPFile[] files = ftpClient.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .toList();
    }
}
