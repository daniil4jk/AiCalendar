package ru.daniil4jk.aicalendar.web.security.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Data
@Configuration
@ConfigurationProperties("secret.google")
public class GoogleTokenConfig {
    private String androidClientId;

    @PostConstruct
    private void check() {
        if (!StringUtils.hasText(androidClientId)) {
            throw new IllegalArgumentException("secret.google.android-client-id must be sat");
        }
    }
    
    @Bean
    public GoogleIdTokenVerifier verifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory()
                )
                .setAudience(Collections.singletonList(
                        androidClientId
                ))
                .build();
    }
}
