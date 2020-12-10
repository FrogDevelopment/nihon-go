package com.frogdevelopment.authentication.application.token;

import com.frogdevelopment.authentication.application.user.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
class RefreshTokenVerifier {

    private final JwtUserDetailsService jwtUserDetailsService;

    RefreshTokenVerifier(JwtUserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    void verify(Claims claims) {
        verifyRevokedToken(claims.getId());
    }

    private void verifyRevokedToken(String jti) {
        Objects.requireNonNull(jti, "Required jti !!!");

        var isRevoked = jwtUserDetailsService.isRevoked(jti);
        if (isRevoked) {
            throw new CredentialsExpiredException("Token has been revoked !!");
        }
    }

}
