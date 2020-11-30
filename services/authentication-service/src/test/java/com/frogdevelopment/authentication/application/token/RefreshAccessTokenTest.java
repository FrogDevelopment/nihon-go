package com.frogdevelopment.authentication.application.token;

import com.frogdevelopment.authentication.application.user.JwtUserDetailsService;
import com.frogdevelopment.jwt.ResolveClaimsFromToken;
import com.frogdevelopment.jwt.RetrieveTokenFromRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class RefreshAccessTokenTest {

    private static final String USERNAME = "USERNAME";
    private static final String TIME_ZONE = "Europe/Paris";

    @InjectMocks
    private RefreshAccessToken refreshAccessToken;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private JwtUserDetailsService jwtUserDetailsService;
    @Mock
    private ResolveClaimsFromToken resolveClaimsFromToken;
    @Mock
    private RefreshTokenVerifier refreshTokenVerifier;
    @Mock
    private RetrieveTokenFromRequest retrieveTokenFromRequest;

    @Test
    void refreshToken_shouldReturn_null_when_incorrect_request() {
        // given
        var request = new MockHttpServletRequest();
        given(retrieveTokenFromRequest.call(request)).willReturn(null);

        // when
        var username = refreshAccessToken.call(TIME_ZONE, request);

        // then
        assertNull(username);
        then(jwtUserDetailsService).shouldHaveNoInteractions();
        then(tokenProvider).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_should_return_new_token() {
        // given
        Claims claims = new DefaultClaims();
        claims.setSubject(USERNAME);

        var token = Jwts.builder()
                .setClaims(claims)
                .compact();

        var request = new MockHttpServletRequest();
        given(retrieveTokenFromRequest.call(request)).willReturn(token);
        given(resolveClaimsFromToken.call(token)).willReturn(claims);
        var user = new User(USERNAME, "", List.of());
        given(jwtUserDetailsService.loadUserByUsername(USERNAME)).willReturn(user);
        given(tokenProvider.createAccessToken(user, TIME_ZONE)).willReturn(Token.builder()
                .issuer("test")
                .subject(USERNAME)
                .chronoUnit(DAYS)
                .timeZone(TIME_ZONE)
                .signingKey("signing-key")
                .build());

        // when
        var refreshedToken = refreshAccessToken.call(TIME_ZONE, request);

        // then
        assertNotNull(refreshedToken);
    }

}
