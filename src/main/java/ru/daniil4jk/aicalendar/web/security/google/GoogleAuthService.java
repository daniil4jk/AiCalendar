package ru.daniil4jk.aicalendar.web.security.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.daniil4jk.aicalendar.db.user.UserEntity;
import ru.daniil4jk.aicalendar.db.user.UserEntityRepository;
import ru.daniil4jk.aicalendar.db.user.UserPrincipal;
import ru.daniil4jk.aicalendar.web.security.jwt.JwtProvider;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleAuthService {
    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider tokenProvider;
    private final GoogleIdTokenVerifier googleTokenVerifier;

    @Transactional
    public String authenticate(String idToken) {
        GoogleIdToken.Payload payload = getTokenPayload(idToken);

        UserEntity user = userRepository.findByName(payload.getEmail())
                .or(() -> userRepository.findByOauth2Id(payload.getSubject()))
                .orElseGet(() -> registerNewUser(payload));

        UserPrincipal principal = new UserPrincipal(user);

        return tokenProvider.generateToken(principal);
    }

    private GoogleIdToken.Payload getTokenPayload(String idToken) throws IllegalArgumentException {
        GoogleIdToken token;
        try {
            token = googleTokenVerifier.verify(idToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (token == null) {
            throw new IllegalArgumentException("token is incorrect");
        }

        return token.getPayload();
    }

    private UserEntity registerNewUser(GoogleIdToken.Payload payload) {
        UserEntity newUser = UserEntity.builder()
                .name(payload.getEmail())
                .oauth2Id(payload.getSubject())
                .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                .build();

        return userRepository.save(newUser);
    }
}