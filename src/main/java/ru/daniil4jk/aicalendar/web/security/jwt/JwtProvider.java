package ru.daniil4jk.aicalendar.web.security.jwt;

public interface JwtProvider {
    String generateToken(String username);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
