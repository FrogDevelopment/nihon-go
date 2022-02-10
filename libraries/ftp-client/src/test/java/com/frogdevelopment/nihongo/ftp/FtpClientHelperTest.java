package com.frogdevelopment.nihongo.ftp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.net.ftp.FTPClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
class FtpClientHelperTest {

    private static final FakeFtpServer FAKE_FTP_SERVER = new FakeFtpServer();

    private static FtpClientHelper ftpClientHelper;

    @BeforeAll
    static void setup() throws IOException {
        final var userAccount = new UserAccount("user", "password", "/data");
        FAKE_FTP_SERVER.addUserAccount(userAccount);

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new FileEntry("/data/foobar.txt", "abcdef 1234567890"));
        FAKE_FTP_SERVER.setFileSystem(fileSystem);
        FAKE_FTP_SERVER.setServerControlPort(0);

        FAKE_FTP_SERVER.start();

        var ftpProperties = new FtpProperties();
        ftpProperties.setServer("localhost");
        ftpProperties.setPort(FAKE_FTP_SERVER.getServerControlPort());
        ftpProperties.setUser(userAccount.getUsername());
        ftpProperties.setPassword(userAccount.getPassword());

        ftpClientHelper = new FtpClientHelper(ftpProperties, new FTPClient());
        ftpClientHelper.open();
    }

    @AfterAll
    static void teardown() throws IOException {
        ftpClientHelper.close();
        FAKE_FTP_SERVER.stop();
    }

    @Test
    void givenExistingRemoteFile_whenDownloading_thenItIsOnTheLocalFilesystem() throws IOException {
        // when
        ftpClientHelper.downloadFile("/data/foobar.txt", "downloaded_file.txt");

        // then
        final var file = new File("downloaded_file.txt");
        assertThat(file).exists();
        file.delete(); // cleanup
    }

    @Test
    void givenMissingRemoteFile_whenDownloading_thenItShouldThrowAnException() {
        // when
        final var caughtException = Assertions.catchException(() -> ftpClientHelper.downloadFile("/data/no_present.txt", "downloaded_file.txt"));

        // then
        assertThat(caughtException)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to download file from /data/no_present.txt");
    }

    @Test
    void givenLocalFile_whenUploadingIt_thenItExistsOnRemoteLocation() throws URISyntaxException, IOException {
        // given
        var file = new File(getClass().getClassLoader().getResource("ftp/baz.txt").toURI());

        // when
        ftpClientHelper.storeFile("/buz.txt", file);

        // then
        assertThat(FAKE_FTP_SERVER.getFileSystem().exists("/buz.txt")).as("File exist on remote location").isTrue();
    }

    @Test
    void givenRemoteFile_whenListingRemoteFiles_thenItIsContainedInList() throws IOException {
        // when
        var files = ftpClientHelper.listFiles("/data");

        // then
        assertThat(files).contains("foobar.txt");
    }
}
