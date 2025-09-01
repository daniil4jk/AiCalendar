package ru.daniil4jk.aicalendar.db.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface EventMapper {
    default EventEntity toEntity(EventLocalizedDto dto, ZoneId zone) {
        return toEntity(
                dto,
                TimeMapper.toInstant(dto.getStart(), zone),
                TimeMapper.toInstant(dto.getEnd(), zone)
        );
    }

    @Mapping(source = "start", target = "start")
    @Mapping(source = "end", target = "end")
    EventEntity toEntity(EventLocalizedDto dto, Instant start, Instant end);

    default EventLocalizedDto toDto(EventEntity entity, ZoneId zone) {
        return toDto(
                entity,
                TimeMapper.toLocal(entity.getStart(), zone),
                TimeMapper.toLocal(entity.getEnd(), zone)
        );
    }

    @Mapping(source = "start", target = "start")
    @Mapping(source = "end", target = "end")
    EventLocalizedDto toDto(EventEntity entity, LocalDateTime start, LocalDateTime end);

    EventAiDto toAiDto(EventLocalizedDto dto);

    EventLocalizedDto toDto(EventAiDto dto);
}

