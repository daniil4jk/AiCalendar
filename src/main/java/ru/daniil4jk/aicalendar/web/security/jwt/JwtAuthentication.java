package ru.daniil4jk.aicalendar.web.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.daniil4jk.aicalendar.web.security.user.UserPrincipal;

import java.util.Collection;

public class JwtAuthentication extends UsernamePasswordAuthenticationToken {
    public JwtAuthentication(UserDetails principal, String token) {
        super(principal, token, principal.getAuthorities());
    }

    public JwtAuthentication(Object principal, String token,
                             Collection<? extends GrantedAuthority> authorities) {
        super(principal, token, authorities);
    }

    @Override
    public UserPrincipal getPrincipal() {
        return (UserPrincipal) super.getPrincipal();
    }
}
