package com.frogdevelopment.authentication.config.security;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ACCESS_EXPIRATION_DATE;
import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ACCESS_TOKEN;
import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.CLIENT_ID;
import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ID_EXPIRATION_DATE;
import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ID_TOKEN;
import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.REFRESH_EXPIRATION_DATE;
import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.REFRESH_TOKEN;
import static com.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.TIME_ZONE;
import static com.frogdevelopment.authentication.config.security.JwtSecurityConfiguration.LOGIN_ENTRY_POINT;
import static com.frogdevelopment.authentication.config.security.JwtSecurityConfiguration.LOGOUT_ENTRY_POINT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integrationTest")
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn401WhenLoginWithBadCredentials() throws Exception {
        // given
        var requestBuilder = post(LOGIN_ENTRY_POINT)
                .param(SPRING_SECURITY_FORM_USERNAME_KEY, "user")
                .param(SPRING_SECURITY_FORM_PASSWORD_KEY, "bla bla bla")
                .accept(APPLICATION_JSON);

        // when
        var resultActions = this.mockMvc.perform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void shouldReturn200WithTokenWhenLoginWithGoodCredentials() throws Exception {
        // given
        var requestBuilder = post(LOGIN_ENTRY_POINT)
                .param(SPRING_SECURITY_FORM_USERNAME_KEY, "admin")
                .param(SPRING_SECURITY_FORM_PASSWORD_KEY, "security_is_fun")
                .param(CLIENT_ID, UUID.randomUUID().toString())
                .param(TIME_ZONE, "Europe/Paris")
                .accept(APPLICATION_JSON);

        // when
        var resultActions = this.mockMvc.perform(requestBuilder);
        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$." + ACCESS_TOKEN).isString())
                .andExpect(jsonPath("$." + ACCESS_EXPIRATION_DATE).isString())
                .andExpect(jsonPath("$." + REFRESH_TOKEN).isString())
                .andExpect(jsonPath("$." + REFRESH_EXPIRATION_DATE).isString())
                .andExpect(jsonPath("$." + ID_TOKEN).isString())
                .andExpect(jsonPath("$." + ID_EXPIRATION_DATE).isString())
        ;
    }

    @Test
    void shouldReturn200WhenLogout() throws Exception {
        // given
        var requestBuilder = post(LOGOUT_ENTRY_POINT)
                .accept(APPLICATION_JSON);

        // when
        var resultActions = this.mockMvc.perform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
