package ru.daniil4jk.aicalendar.web.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.daniil4jk.aicalendar.db.user.UserEntity;
import ru.daniil4jk.aicalendar.db.user.UserEntityRepository;
import ru.daniil4jk.aicalendar.web.security.RoleHolder;

@Service
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {
    private final RoleHolder roleHolder = new RoleHolder();
    private final UserEntityRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByName(username)
                .map(this::createDetailsByEntity)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "username \"%s\" not exist".formatted(username)
                ));
    }

    private UserDetails createDetailsByEntity(UserEntity entity) {
        return new UserPrincipal(entity, roleHolder.getRolesFor(entity));
    }
}
