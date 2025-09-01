package ru.daniil4jk.aicalendar.db.event;

import jakarta.persistence.*;
import lombok.*;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.time.Instant;

@Entity(name = "event")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @Column(name = "start_time", nullable = false)
    private Instant start;

    @Column(name = "end_time", nullable = false)
    private Instant end;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
