package ru.daniil4jk.aicalendar.db.event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeMapper {
    private TimeMapper() {}

    public static Instant toInstant(LocalDateTime local, ZoneId zone) {
        return local.toInstant(zone.getRules().getOffset(local));
    }

    public static LocalDateTime toLocal(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(
                instant,
                zoneId.getRules().getOffset(instant)
        );
    }
}
