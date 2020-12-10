package com.frogdevelopment.nihongo.sentences.config;

import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import com.frogdevelopment.jwt.ResolveTokenToAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    JwtProcessTokenFilter jwtProcessTokenFilter(ResolveTokenToAuthentication tokenToAuthentication) {
        return new JwtProcessTokenFilter(tokenToAuthentication);
    }
}
