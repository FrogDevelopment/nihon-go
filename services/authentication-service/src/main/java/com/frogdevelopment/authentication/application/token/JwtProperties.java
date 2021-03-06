package com.frogdevelopment.authentication.application.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.InvalidParameterException;

@Data
@Component
@RefreshScope
@Configuration
@ConfigurationProperties("security.jwt.token")
public class JwtProperties {

    private String signingKey;
    private long accessTokenExpiration = 5;
    private long refreshTokenExpiration = 10;
    private long iDTokenExpiration = 10;

    @PostConstruct
    void init() {
        if (signingKey == null) {
            throw new InvalidParameterException("security.jwt.token.signing-key required !!");
        }
    }
}
