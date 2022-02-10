package com.frogdevelopment.nihongo.ftp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.OpenMode;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SftpClientHelper implements AutoCloseable {

    private final FtpProperties ftpProperties;
    private SSHClient sshClient;
    private SFTPClient sftpClient;

    public void open() throws IOException {
        sshClient = new SSHClient();
        sshClient.addHostKeyVerifier(new PromiscuousVerifier());

        sshClient.connect(ftpProperties.getServer(), ftpProperties.getPort());
        if (!sshClient.isConnected()) {
            throw new IOException("Connection failed");
        }

        sshClient.authPassword(ftpProperties.getUser(), ftpProperties.getPassword());
        if (sshClient.isAuthenticated()) {
            sftpClient = sshClient.newSFTPClient();
        } else {
            // Close connection and throw authentication error
            sshClient.close();
            throw new IOException("Authentication failed");
        }
    }

    @Override
    @PreDestroy
    public void close() throws IOException {
        if (sshClient != null && sshClient.isConnected()) {
            log.debug("Closing FTP client");
            if (sftpClient!= null) {
                sftpClient.close();
            }
            sshClient.close();
        }
    }

    // have a look at https://github.com/hierynomus/sshj/issues/734#issuecomment-944507311

    public void downloadFile(final String source, final String destination) throws IOException {
        sftpClient.get(source, destination);
    }

    public InputStream retrieveFileStream(final String source) throws IOException {
        try (RemoteFile file = sftpClient.getSFTPEngine().open(source, EnumSet.of(OpenMode.READ))) {
            return file.new RemoteFileInputStream();
        }
    }

    public void storeFile(final String path, final File file) throws IOException {
        sftpClient.put(new FileSystemFile(file), path);
    }

    public List<String> listFiles(String path) throws IOException {
        return sftpClient.ls(path)
                .stream()
                .map(RemoteResourceInfo::getName)
                .toList();
    }
}
