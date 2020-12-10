package com.frogdevelopment.authentication.api;

import com.frogdevelopment.authentication.application.token.RefreshAccessToken;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SpringJUnitWebConfig
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integrationTest")
class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefreshAccessToken refreshAccessToken;

    @Test
    @WithMockUser
    void addShouldCreateWhenAdmin() throws Exception {
        // given
        given(this.refreshAccessToken.call(anyString(), any())).willReturn("my-new-token");

        var requestBuilder = get("/token/refresh")
                .param("time_zone", "Europe/Paris")
                .contentType(TEXT_PLAIN_VALUE)
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        // when
        var resultActions = this.mockMvc
                .perform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("my-new-token"))
        ;

        then(refreshAccessToken).should().call(eq("Europe/Paris"), any());
    }

}
