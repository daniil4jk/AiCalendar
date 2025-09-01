package ru.daniil4jk.aicalendar.db.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventLocalizedDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
}
