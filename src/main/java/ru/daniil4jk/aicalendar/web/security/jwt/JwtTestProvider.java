package ru.daniil4jk.aicalendar.web.security.jwt;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.daniil4jk.aicalendar.db.user.UserEntity;
import ru.daniil4jk.aicalendar.db.user.UserEntityRepository;
import ru.daniil4jk.aicalendar.db.user.UserPrincipal;

import java.time.ZoneId;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "app.feature.enabled", havingValue = "true", matchIfMissing = false)
@AutoConfigureBefore(JwtProductionProvider.class)
public class JwtTestProvider implements JwtProvider{
    private final String acceptableToken;
    private final UserPrincipal user;

    public JwtTestProvider(JwtConfig config, PasswordEncoder passwordEncoder,
                           UserEntityRepository repository) {
        this.acceptableToken = config.getTestJwtKey();
        user = new UserPrincipal(
                repository.save(UserEntity.builder()
                        .name("testUser")
                        .oauth2Id(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .timeZone(ZoneId.systemDefault())
                        .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                .build())
        );
    }

    @Override
    public String generateToken(UserPrincipal principal) {
        return acceptableToken;
    }

    @Override
    public String getUsernameFromToken(String token) {
        if (acceptableToken.equals(token)) {
            return user.getUsername();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean validateToken(String token) {
        return acceptableToken.equals(token);
    }
}
