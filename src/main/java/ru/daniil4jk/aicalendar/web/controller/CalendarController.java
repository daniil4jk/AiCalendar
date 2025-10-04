package ru.daniil4jk.aicalendar.web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.daniil4jk.aicalendar.db.event.EventLocalizedDto;
import ru.daniil4jk.aicalendar.db.event.EventService;
import ru.daniil4jk.aicalendar.web.security.user.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class CalendarController {
    private final EventService service;

    @GetMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<EventLocalizedDto>> getEvents(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam("fromDate") LocalDate from,
            @RequestParam("toDate") LocalDate to
    ) {
        return ResponseEntity.ok(service.getFor(user.getInherit(), from, to));
    }

    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<EventLocalizedDto> postEvent(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody EventLocalizedDto dto
    ) {
        return ResponseEntity.ok(service.post(user.getInherit(), dto));
    }

    @PatchMapping("/{eventId}")
    @SecurityRequirement(name = "JWT")
    public void patchEvent(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody EventLocalizedDto dto,
            @PathVariable Long eventId
    ) {
        service.patch(user.getInherit(), dto, eventId);
    }

    @DeleteMapping("/{eventId}")
    @SecurityRequirement(name = "JWT")
    public void deleteEvent(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long eventId
    ) {
        service.delete(user.getInherit(), eventId);
    }
}
