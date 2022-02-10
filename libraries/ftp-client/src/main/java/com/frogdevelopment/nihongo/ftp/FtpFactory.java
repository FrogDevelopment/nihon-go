package com.frogdevelopment.nihongo.ftp;

import org.apache.commons.net.ftp.FTPClient;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class FtpFactory {

    @Bean
    FTPClient ftpClient(){
        return new FTPClient();
    }

    @Bean(preDestroy = "close")
    FtpClientHelper ftpClientHelper(final FtpProperties ftpProperties, final  FTPClient ftpClient) {
        return new FtpClientHelper(ftpProperties, ftpClient);
    }
}
