package ru.daniil4jk.aicalendar.db.event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventEntityRepository repository;
    private final EventMapper mapper;

    public List<EventLocalizedDto> getFor(UserEntity user, LocalDate from, LocalDate to) {
        ZoneId timeZone = user.getTimeZone();

        LocalDateTime fromTime = from.atStartOfDay();
        LocalDateTime toTime = to.plusDays(1).atStartOfDay(); //at end of day

        return repository.findAllByUserAndStartAfterAndEndBefore(
                user,
                TimeMapper.toInstant(fromTime, timeZone),
                TimeMapper.toInstant(toTime, timeZone)
                ).stream()
                .sorted()
                .map(entity -> mapper.toDto(entity, timeZone))
                .toList();
    }
    
    public EventLocalizedDto post(UserEntity user, EventLocalizedDto dto) {
        EventEntity entity = mapper.toEntity(dto, user.getTimeZone());
        entity.setUser(user);
        EventEntity save = repository.save(entity);
        return mapper.toDto(save, user.getTimeZone());
    }

    @Transactional
    public void patch(UserEntity user, EventLocalizedDto dto, Long eventId) {
        EventEntity event = getEvent(eventId);
        controlAccess(user, event);

        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setStart(TimeMapper.toInstant(dto.getStart(), user.getTimeZone()));
        event.setEnd(TimeMapper.toInstant(dto.getEnd(), user.getTimeZone()));

        repository.save(event);
    }

    @Transactional
    public void delete(UserEntity user, Long eventId) {
        EventEntity event = getEvent(eventId);
        controlAccess(user, event);
        repository.delete(event);
    }

    private EventEntity getEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event with id %s not exist"
                        .formatted(eventId)));
    }

    private void controlAccess(UserEntity user, EventEntity event) {
        if (!user.getId().equals(event.getUser().getId())) {
            throw new SecurityException("user %s has`nt access for event %s"
                    .formatted(user, event));
        }
    }
}
