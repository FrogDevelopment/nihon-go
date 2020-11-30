package com.frogdevelopment.authentication.application.token;

import com.frogdevelopment.authentication.application.token.Token.TokenBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static java.time.temporal.ChronoUnit.CENTURIES;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unitTest")
class TokenTest {

    @Test
    void shouldBuildWhenAllRequiredFieldsAreSet() {
        var token = givenCorrectTokenBuilder()
                .build();

        var jwt = token.toJwt();

        assertNotNull(jwt);
    }

    @Test
    void shouldThrowAnExceptionWhen_issuer_null() {
        assertThrows(NullPointerException.class, () -> givenCorrectTokenBuilder()
                .issuer(null)
                .build());
    }

    @Test
    void shouldThrowAnExceptionWhen_subject_null() {
        assertThrows(NullPointerException.class, () -> givenCorrectTokenBuilder()
                .subject(null)
                .build());
    }

    @Test
    void shouldThrowAnExceptionWhen_chronoUnit_null() {
        assertThrows(NullPointerException.class, () -> givenCorrectTokenBuilder()
                .chronoUnit(null)
                .build());
    }

    @Test
    void shouldThrowAnExceptionWhen_signingKey_null() {
        assertThrows(NullPointerException.class, () -> givenCorrectTokenBuilder()
                .signingKey(null)
                .build());
    }

    @Test
    void shouldThrowAnExceptionWhen_timeZone_null() {
        assertThrows(NullPointerException.class, () -> givenCorrectTokenBuilder()
                .timeZone(null)
                .build());
    }

    private TokenBuilder givenCorrectTokenBuilder() {
        return Token.builder()
                .subject("test")
                .issuer("test")
                .expiration(10)
                .chronoUnit(CENTURIES)
                .timeZone("Europe/Paris")
                .signingKey("SECRET_KEY");
    }
}
