package com.frogdevelopment.nihongo.export;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.frogdevelopment.nihongo.FtpClient;

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
    public CompressExport compress() {
        return new CompressExport();
    }

    @Bean
    public CopyOut copyOut(final CompressExport compressExport) {
        return new CopyOut(compressExport);
    }

    @Bean
    public ExportData exportData(@Value("${frog.ftp.remote-path}") final String remotePath,
                                 final DataSource dataSource,
                                 final FtpClient ftpClient,
                                 final CopyOut copyOut) {
        return new ExportData(remotePath, dataSource, ftpClient, copyOut);
    }

}
