package ru.daniil4jk.aicalendar.db.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventAiDto {
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
}
