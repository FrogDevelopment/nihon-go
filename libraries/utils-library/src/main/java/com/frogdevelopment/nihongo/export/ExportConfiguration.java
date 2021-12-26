package com.frogdevelopment.nihongo.export;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.frogdevelopment.nihongo.ftp.FtpClient;
import com.frogdevelopment.nihongo.ftp.FtpProperties;

@Configuration
public class ExportConfiguration {

    @Bean
    public FtpClient ftpClient(final FtpProperties ftpProperties) {
        return new FtpClient(ftpProperties);
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
    public ExportData exportData(final FtpProperties ftpProperties,
                                 final DataSource dataSource,
                                 final FtpClient ftpClient,
                                 final CopyOut copyOut) {
        return new ExportData(ftpProperties.getRemotePath(), dataSource, ftpClient, copyOut);
    }

}
