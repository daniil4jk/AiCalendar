package ru.daniil4jk.aicalendar.db.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.daniil4jk.aicalendar.web.security.SecurityConfig;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(UserEntity inherit) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(SecurityConfig.USER_ROLE);
    }

    @Override
    public String getPassword() {
        return inherit.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return inherit.getName();
    }
}
