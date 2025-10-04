package ru.daniil4jk.aicalendar.ai.function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.daniil4jk.aicalendar.db.event.*;
import ru.daniil4jk.aicalendar.db.user.UserEntity;
import ru.daniil4jk.aicalendar.web.security.user.UserPrincipal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Service
@RequiredArgsConstructor
public class EventFunctions {
    private final EventMapper mapper;
    private final EventEntityRepository eventRepository;

    @Getter
    private volatile List<EventLocalizedDto> events;

    @Tool(name = "getTime", description = "Returns current time for user`s TimeZone")
    public LocalDateTime getTime() {
        return LocalDateTime.now(getUserFromSecurityContext().getTimeZone());
    }

    @Tool(name = "createEvent", description = "Create a new event with a name, description, start and end time.")
    public String createEvent(EventAiDto event) {
        if (events == null) {
            synchronized (this) {
                if (events == null) {
                    events = new ArrayList<>();
                }
            }
        }

        EventEntity entity = persistEvent(event);
        EventLocalizedDto dto = mapper.toDto(event);
        dto.setId(entity.getId());
        events.add(dto);

        return event.toString();
    }

    private EventEntity persistEvent(EventAiDto aiDto) {
        UserEntity user = getUserFromSecurityContext();

        EventEntity entity = mapper.toEntity(
                mapper.toDto(aiDto),
                user.getTimeZone()
        );

        entity.setUser(user);
        entity = eventRepository.save(entity);
        user.getEvents().add(entity);

        return entity;
    }

    @Tool(name = "getEventsInInterval", description = "Returns all events from start to end time")
    public String getEventsInInterval(LocalDateTime start, LocalDateTime end) {
        UserEntity user = getUserFromSecurityContext();

        return eventRepository
                .findAllByUserAndStartAfterAndEndBefore(
                        user,
                        TimeMapper.toInstant(start, user.getTimeZone()),
                        TimeMapper.toInstant(end, user.getTimeZone())
                ).stream()
                .map(entity -> mapper.toDto(entity, user.getTimeZone()))
                .map(mapper::toAiDto)
                .toString();
    }

    private UserEntity getUserFromSecurityContext() {
        UserPrincipal principal = (UserPrincipal)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        return principal.getInherit();
    }
}
