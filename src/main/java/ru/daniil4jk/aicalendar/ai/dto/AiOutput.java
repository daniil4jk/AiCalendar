package ru.daniil4jk.aicalendar.ai.dto;

import ru.daniil4jk.aicalendar.db.event.EventLocalizedDto;
import ru.daniil4jk.aicalendar.db.message.MessageDto;

import java.util.List;

public record AiOutput(
        MessageDto request,
        MessageDto response,
        List<EventLocalizedDto> events
) {}
