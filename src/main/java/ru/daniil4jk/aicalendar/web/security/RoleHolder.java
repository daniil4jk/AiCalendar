package ru.daniil4jk.aicalendar.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.util.Collection;
import java.util.Collections;

public class RoleHolder {
    private static final GrantedAuthority USER_ROLE = new SimpleGrantedAuthority("ROLE_USER");

    public Collection<? extends GrantedAuthority> getRolesFor(UserEntity user) {
        return Collections.singleton(USER_ROLE);
    }
}
