package com.frogdevelopment.nihongo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.DisposableBean;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.frogdevelopment.nihongo.LoggingOutputStream.LogLevel.INFO;

@Slf4j
public class FtpClient implements DisposableBean {

    private final String server;
    private final int port;
    private final String user;
    private final String password;

    private final FTPClient ftp;

    public FtpClient(final String server, final int port, final String user, final String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;

        this.ftp = new FTPClient();
        this.ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(new LoggingOutputStream(log, INFO))));
    }

    public void open() throws IOException {
        ftp.connect(server, port);
        final int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.login(user, password);
    }

    public void close() throws IOException {
        if (ftp != null) {
            ftp.logout();
            ftp.disconnect();
        }
    }

    public void destroy() throws IOException {
        close();
    }

    public void downloadFile(final String source, final String destination) throws IOException {
        ftp.retrieveFile(source, new FileOutputStream(destination));
    }

    public void putFileToPath(final String path, final File file) throws IOException {
        try (final var inputStream = new FileInputStream(file)) {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
            ftp.setCharset(StandardCharsets.UTF_8);
            ftp.enterLocalPassiveMode();
            ftp.storeFile(path, inputStream);
        }
    }

    public Collection<String> listFiles(final String path) throws IOException {
        final FTPFile[] files = ftp.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }
}
