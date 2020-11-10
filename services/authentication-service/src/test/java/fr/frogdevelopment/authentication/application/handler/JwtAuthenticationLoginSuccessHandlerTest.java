package fr.frogdevelopment.authentication.application.handler;

import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ACCESS_EXPIRATION_DATE;
import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ACCESS_TOKEN;
import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.CLIENT_ID;
import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ID_EXPIRATION_DATE;
import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.ID_TOKEN;
import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.REFRESH_EXPIRATION_DATE;
import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.REFRESH_TOKEN;
import static fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler.TIME_ZONE;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.frogdevelopment.authentication.application.token.Token;
import fr.frogdevelopment.authentication.application.token.TokenProvider;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationLoginSuccessHandlerTest {

    private static final String EUROPE_PARIS = "Europe/Paris";

    @InjectMocks
    private JwtAuthenticationLoginSuccessHandler jwtAuthenticationLoginSuccessHandler;

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private Authentication authentication;
    @Mock
    private PrintWriter writer;

    private String clientID;

    @BeforeEach
    void beforeEach() throws IOException {
        given(httpServletResponse.getWriter()).willReturn(writer);
        clientID = UUID.randomUUID().toString();
        given(httpServletRequest.getParameter(anyString())).will(invocation -> {
            String param = invocation.getArgument(0);
            if (CLIENT_ID.endsWith(param)) {
                return clientID;
            } else if (TIME_ZONE.equals(param)) {
                return EUROPE_PARIS;
            }
            return null;
        });
    }

    @Captor
    private ArgumentCaptor<Map<String, Object>> bodyTokenCaptor;

    @Test
    void shouldWriteTheTokenToTheBodyAndReturnOK() throws IOException {
        // given
        givenAccessToken();
        givenRefreshToken();
        givenIDToken();

        // when
        jwtAuthenticationLoginSuccessHandler
                .onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);

        // then
        verify(objectMapper).writeValue(eq(writer), bodyTokenCaptor.capture());
        assertThat(bodyTokenCaptor.getValue()).containsOnlyKeys(
                ACCESS_TOKEN,
                ACCESS_EXPIRATION_DATE,
                REFRESH_TOKEN,
                REFRESH_EXPIRATION_DATE,
                ID_TOKEN,
                ID_EXPIRATION_DATE
        );

        verify(httpServletResponse).setStatus(SC_OK);
        verify(httpServletResponse).flushBuffer();
        verify(writer).close();
    }

    @Test
    void shouldReturnErrorWhenException() throws IOException {
        // given
        givenAccessToken();
        givenRefreshToken();
        givenIDToken();
        doThrow(IOException.class).when(objectMapper).writeValue(eq(writer), any());

        // when
        jwtAuthenticationLoginSuccessHandler
                .onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);

        // then
        verify(httpServletResponse).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(writer).close();
    }

    private Token givenToken() {
        return Token.builder()
                .issuer("test")
                .subject("test")
                .audience(clientID)
                .expiration(10)
                .timeZone(EUROPE_PARIS)
                .chronoUnit(ChronoUnit.DAYS)
                .signingKey("secret-key")
                .build();
    }

    private void givenAccessToken() {
        given(tokenProvider.createAccessToken(authentication, EUROPE_PARIS)).willReturn(givenToken());
    }

    private void givenRefreshToken() {
        given(tokenProvider.createRefreshToken(authentication, EUROPE_PARIS)).willReturn(givenToken());
    }

    private void givenIDToken() {
        given(tokenProvider.createIDToken(authentication, clientID, EUROPE_PARIS)).willReturn(givenToken());
    }
}
