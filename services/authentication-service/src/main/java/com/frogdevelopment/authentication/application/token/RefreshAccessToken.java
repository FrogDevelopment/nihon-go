package com.frogdevelopment.authentication.application.token;

import com.frogdevelopment.jwt.ResolveClaimsFromToken;
import com.frogdevelopment.jwt.RetrieveTokenFromRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RefreshAccessToken {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final ResolveClaimsFromToken resolveClaimsFromToken;
    private final RefreshTokenVerifier refreshTokenVerifier;
    private final RetrieveTokenFromRequest retrieveTokenFromRequest;

    public RefreshAccessToken(TokenProvider tokenProvider,
                              JdbcUserDetailsManager userDetailsService,
                              ResolveClaimsFromToken resolveClaimsFromToken,
                              RefreshTokenVerifier refreshTokenVerifier,
                              RetrieveTokenFromRequest retrieveTokenFromRequest) {
        this.resolveClaimsFromToken = resolveClaimsFromToken;
        this.refreshTokenVerifier = refreshTokenVerifier;
        this.retrieveTokenFromRequest = retrieveTokenFromRequest;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public String call(String timeZone, HttpServletRequest request) {

        var token = retrieveTokenFromRequest.call(request);
        if (token == null) {
            return null;
        }

        var claims = resolveClaimsFromToken.call(token);

        refreshTokenVerifier.verify(claims);

        var username = claims.getSubject();
        var userDetails = userDetailsService.loadUserByUsername(username);

        return tokenProvider.createAccessToken(userDetails, timeZone).toJwt();
    }

}
