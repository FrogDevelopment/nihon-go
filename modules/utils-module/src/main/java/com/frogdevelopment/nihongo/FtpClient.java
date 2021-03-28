package com.frogdevelopment.nihongo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.DisposableBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FtpClient implements DisposableBean {

    private final String server;
    private final int port;
    private final String user;
    private final String password;

    private FTPClient ftp;

    public void open() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

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
        ftp.storeFile(path, new FileInputStream(file));
    }

    public Collection<String> listFiles(final String path) throws IOException {
        final FTPFile[] files = ftp.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }
}
