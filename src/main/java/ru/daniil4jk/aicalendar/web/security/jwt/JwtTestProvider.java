package ru.daniil4jk.aicalendar.web.security.jwt;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "secret.jwt.test", havingValue = "true", matchIfMissing = false)
@AutoConfigureBefore(JwtProductionProvider.class)
public class JwtTestProvider implements JwtProvider{
    private final String acceptableToken;
    private final String username;

    public JwtTestProvider(JwtConfig config) {
        this.acceptableToken = config.getTestJwtKey();
        username = "test";
    }

    @Override
    public String generateToken(String username) {
        return acceptableToken;
    }

    @Override
    public String getUsernameFromToken(String token) {
        if (acceptableToken.equals(token)) {
            return username;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean validateToken(String token) {
        return acceptableToken.equals(token);
    }
}
