package com.frogdevelopment.nihongo.ftp;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class SftpFactory {

    @Singleton
    @Bean(preDestroy = "close")
    SftpClientHelper sftpClientHelper(final FtpProperties ftpProperties) {
        return new SftpClientHelper(ftpProperties);
    }
}
