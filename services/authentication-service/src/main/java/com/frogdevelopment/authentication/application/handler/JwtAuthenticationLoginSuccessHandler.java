package com.frogdevelopment.authentication.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.authentication.application.token.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Singleton
public class JwtAuthenticationLoginSuccessHandler implements AuthenticationSuccessHandler {

    public static final String CLIENT_ID = "client_id";
    public static final String TIME_ZONE = "time_zone";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ACCESS_EXPIRATION_DATE = "access_expiration_date";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String REFRESH_EXPIRATION_DATE = "refresh_expiration_date";
    public static final String ID_TOKEN = "id_token";
    public static final String ID_EXPIRATION_DATE = "id_expiration_date";

    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    JwtAuthenticationLoginSuccessHandler(ObjectMapper objectMapper,
                                         TokenProvider tokenProvider) {
        this.objectMapper = objectMapper;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) {
        var clientID = httpServletRequest.getParameter(CLIENT_ID);
        var timeZone = httpServletRequest.getParameter(TIME_ZONE);
        var tokenMap = new HashMap<String, Object>();

        var accessToken = tokenProvider.createAccessToken(authentication, timeZone);
        tokenMap.put(ACCESS_TOKEN, accessToken.toJwt());
        tokenMap.put(ACCESS_EXPIRATION_DATE, accessToken.getExpirationDate());

        var refreshToken = tokenProvider.createRefreshToken(authentication, timeZone);
        tokenMap.put(REFRESH_TOKEN, refreshToken.toJwt());
        tokenMap.put(REFRESH_EXPIRATION_DATE, refreshToken.getExpirationDate());

        var idToken = tokenProvider.createIDToken(authentication, clientID, timeZone);
        tokenMap.put(ID_TOKEN, idToken.toJwt());
        tokenMap.put(ID_EXPIRATION_DATE, idToken.getExpirationDate());

        // send token as response
        log.info("Authentication success => write token on body");
        try (var writer = httpServletResponse.getWriter()) {
            objectMapper.writeValue(writer, tokenMap);
            httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(SC_OK);
            httpServletResponse.flushBuffer();
        } catch (IOException e) {
            log.error("Error while writing authentication token to response", e);
            httpServletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
    }
}
