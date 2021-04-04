package com.frogdevelopment.nihongo.lessons.config;

import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import com.frogdevelopment.jwt.ResolveTokenToAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ApplicationConfiguration {

    @Bean
    public JwtProcessTokenFilter jwtProcessTokenFilter(final ResolveTokenToAuthentication tokenToAuthentication) {
        return new JwtProcessTokenFilter(tokenToAuthentication);
    }
}
