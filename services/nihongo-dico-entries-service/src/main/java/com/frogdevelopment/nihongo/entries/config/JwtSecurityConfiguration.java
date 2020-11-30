package com.frogdevelopment.nihongo.entries.config;

import com.frogdevelopment.jwt.JwtAuthorizationConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtSecurityConfiguration extends JwtAuthorizationConfigurerAdapter {

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider();
    }
}
