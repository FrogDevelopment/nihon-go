package com.frogdevelopment.authentication.application.token;

import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import static com.frogdevelopment.authentication.application.DateUtils.toDate;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.of;

@Slf4j
@Getter
@Builder
public class Token {

    private final String id;
    @NonNull
    private final String issuer;
    private final String audience;
    @NonNull
    private final String subject;
    private final Map<String, Object> claims;
    private final long expiration;
    @NonNull
    private final ChronoUnit chronoUnit;
    @NonNull
    private final String timeZone;
    @NonNull
    private final String signingKey;

    private Date expirationDate;

    public String toJwt() {
        log.debug("Building token with timeZone={}", timeZone);
        LocalDateTime issuedAt = now(of(timeZone));
        log.debug("=====> issuedAt={}", issuedAt);
        expirationDate = toDate(issuedAt.plus(expiration, chronoUnit));
        log.debug("=====> expirationDate={}", expirationDate);

        return Jwts.builder()
                .setId(id)
                .setIssuer(issuer)
                .setAudience(audience)
                .setSubject(subject)
                .setIssuedAt(toDate(issuedAt))
                .setExpiration(expirationDate)
                .signWith(HS512, signingKey.getBytes())
                .addClaims(claims)
                .compact();
    }
}
