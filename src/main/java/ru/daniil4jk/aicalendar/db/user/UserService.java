package ru.daniil4jk.aicalendar.db.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository repository;

    public void setTimeZone(UserEntity user, ZoneId zoneId) {
        user.setTimeZone(zoneId);
        repository.save(user);
    }
}
