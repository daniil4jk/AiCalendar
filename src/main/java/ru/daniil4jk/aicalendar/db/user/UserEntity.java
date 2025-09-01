package ru.daniil4jk.aicalendar.db.user;

import jakarta.persistence.*;
import lombok.*;
import ru.daniil4jk.aicalendar.db.chat.ChatEntity;
import ru.daniil4jk.aicalendar.db.event.EventEntity;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Table(name = "\"USER\"")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ToString.Exclude
    private String oauth2Id;

    @ToString.Exclude
    private String passwordHash;

    private ZoneId timeZone;

    public ZoneId getTimeZone() {
        if (timeZone == null) {
            return ZoneId.systemDefault();
        }
        return timeZone;
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST})
    private final List<ChatEntity> chats = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST})
    private final List<EventEntity> events = new ArrayList<>();
}
