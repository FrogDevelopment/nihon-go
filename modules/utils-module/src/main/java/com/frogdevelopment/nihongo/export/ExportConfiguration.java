package com.frogdevelopment.nihongo.export;

import com.frogdevelopment.nihongo.FtpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ExportConfiguration {

    @Bean
    public FtpClient ftpClient(@Value("${frog.ftp.hostname}") final String server,
                               @Value("${frog.ftp.port:21}") final int port,
                               @Value("${frog.ftp.username}") final String user,
                               @Value("${frog.ftp.password}") final String password) {
        return new FtpClient(server, port, user, password);
    }

    @Bean
    public CopyOut copyOut() {
        return new CopyOut();
    }

    @Bean
    public ExportData exportData(@Value("${frog.ftp.remote-path}") final String remotePath,
                                 final DataSource dataSource,
                                 final FtpClient ftpClient,
                                 final CopyOut copyOut) {
        return new ExportData(remotePath, dataSource, ftpClient, copyOut);
    }

}
