package com.frogdevelopment.authentication.application.token;

import com.frogdevelopment.jwt.ResolveClaimsFromToken;
import com.frogdevelopment.jwt.RetrieveTokenFromRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class JwtParser {

    private final ResolveClaimsFromToken resolveClaimsFromToken;
    private final RetrieveTokenFromRequest retrieveTokenFromRequest;

    @Autowired
    public JwtParser(ResolveClaimsFromToken resolveClaimsFromToken,
                     RetrieveTokenFromRequest retrieveTokenFromRequest) {
        this.resolveClaimsFromToken = resolveClaimsFromToken;
        this.retrieveTokenFromRequest = retrieveTokenFromRequest;
    }

    public String getIdFromRefreshToken(HttpServletRequest request) {
        var token = retrieveTokenFromRequest.call(request);
        if (token == null) {
            return null;
        }

        return resolveClaimsFromToken.call(token).getId();
    }

}
