package ru.daniil4jk.aicalendar.web.security.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
@ConditionalOnMissingBean(JwtProvider.class)
public class JwtProductionProvider implements JwtProvider {
    private final JwtConfig.LifeTime timeConfig;
    private final JwtParser parser;
    private final SecretKey key;

    public JwtProductionProvider(JwtConfig config) {
        this.timeConfig = config.getLifeTime();
        this.key = Keys.hmacShaKeyFor(config.getSigningKey().getBytes());
        this.parser = Jwts.parser()
                .verifyWith(key)
                .build();
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiry = now.plus(timeConfig.getValue(), timeConfig.getUnit());

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return parser
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            return parser
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .after(Date.from(Instant.now()));
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("JWT validation error", ex);
            }
            return false;
        }
    }
}