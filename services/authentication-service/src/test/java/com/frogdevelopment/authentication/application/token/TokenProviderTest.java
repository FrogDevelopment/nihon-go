package com.frogdevelopment.authentication.application.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

import static com.frogdevelopment.authentication.application.token.TokenProvider.AUTHORITIES_KEY;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
class TokenProviderTest {

    private static final String ISSUER = "test";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final Set<String> AUTHORITIES = Set.of("ADMIN", "USER");
    private static final String SECRET_KEY = "SECRET_KEY";
    private static final String TIME_ZONE = "Europe/Paris";

    private TokenProvider tokenProvider;

    private final JwtProperties jwtProperties = new JwtProperties();

    @BeforeEach
    void beforeEach() {
        jwtProperties.setSigningKey(SECRET_KEY);
        tokenProvider = new TokenProvider(ISSUER, jwtProperties);
    }

    @Test
    void shouldReturnTokenWhenCreatingAccessTokenFromUserDetails() {
        // given
        var userDetails = givenUserDetails();

        // when
        var token = tokenProvider.createAccessToken(userDetails, TIME_ZONE);

        // then
        thenAccessTokenIsCreatedWithAllNeededData(token);
    }

    @Test
    void shouldReturnTokenWhenCreatingAccessTokenFromAuthentication() {
        // given
        var authentication = givenAuthentication();

        // when
        var token = tokenProvider.createAccessToken(authentication, TIME_ZONE);

        // then
        thenAccessTokenIsCreatedWithAllNeededData(token);
    }

    @Test
    void shouldReturnTokenWhenCreatingRefreshToken() {
        // given
        var authentication = givenAuthentication();

        // when
        var token = tokenProvider.createRefreshToken(authentication, TIME_ZONE);

        // then
        thenRefreshTokenIsCreatedWithAllNeededData(token);
    }

    private UserDetails givenUserDetails() {
        var grantedAuthorities = AUTHORITIES.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(USERNAME, PASSWORD, grantedAuthorities);
    }

    private UsernamePasswordAuthenticationToken givenAuthentication() {
        var grantedAuthorities = AUTHORITIES.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD, grantedAuthorities);
    }

    private void thenAccessTokenIsCreatedWithAllNeededData(Token token) {
        assertThat(token).isNotNull();
        assertThat(token.getId()).isNull();
        assertThat(token.getIssuer()).isEqualTo(ISSUER);
        assertThat(token.getSubject()).isEqualTo(USERNAME);
        assertThat(token.getClaims()).containsEntry(AUTHORITIES_KEY, AUTHORITIES);
        assertThat(token.getExpiration()).isEqualTo(jwtProperties.getAccessTokenExpiration());
        assertThat(token.getSigningKey()).isEqualTo(SECRET_KEY);
    }

    private void thenRefreshTokenIsCreatedWithAllNeededData(Token token) {
        assertThat(token).isNotNull();
        assertThat(token.getId()).isNotBlank();
        assertThat(token.getIssuer()).isEqualTo(ISSUER);
        assertThat(token.getSubject()).isEqualTo(USERNAME);
        assertThat(token.getClaims()).isNull();
        assertThat(token.getExpiration()).isEqualTo(jwtProperties.getRefreshTokenExpiration());
        assertThat(token.getSigningKey()).isEqualTo(SECRET_KEY);
    }

}
