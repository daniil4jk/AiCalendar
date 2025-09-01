package ru.daniil4jk.aicalendar.web.security.jwt;

import ru.daniil4jk.aicalendar.db.user.UserPrincipal;

public interface JwtProvider {
    String generateToken(UserPrincipal principal);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
