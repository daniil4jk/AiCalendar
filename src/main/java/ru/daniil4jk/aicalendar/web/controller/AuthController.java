package ru.daniil4jk.aicalendar.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.daniil4jk.aicalendar.web.security.google.GoogleAuthService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> authenticateGoogle(
            @RequestBody GoogleAuthRequest request
    ) {
        String token = googleAuthService.authenticate(request.idToken());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public record GoogleAuthRequest(String idToken) {}

    public record JwtResponse(String jwt) {}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> onAuthFailed(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Auth failed because: %s".formatted(e.getLocalizedMessage()));
    }
}