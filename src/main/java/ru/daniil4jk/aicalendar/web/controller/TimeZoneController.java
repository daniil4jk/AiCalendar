package ru.daniil4jk.aicalendar.web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.daniil4jk.aicalendar.db.user.UserService;
import ru.daniil4jk.aicalendar.web.security.user.UserPrincipal;

import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/zone")
public class TimeZoneController {
    private final UserService service;

    @PostMapping("/set")
    @SecurityRequirement(name = "JWT")
    public void setZone(@AuthenticationPrincipal UserPrincipal user,
                        @RequestBody String timeZone) {
        service.setTimeZone(user.getInherit(), ZoneId.of(timeZone));
    }
}
