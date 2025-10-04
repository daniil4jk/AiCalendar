package ru.daniil4jk.aicalendar.web.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.util.Collection;

@Data
@AllArgsConstructor
public final class UserPrincipal implements UserDetails {
    private final UserEntity inherit;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return inherit.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return inherit.getName();
    }
}
