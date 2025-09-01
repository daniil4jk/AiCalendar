package ru.daniil4jk.aicalendar.db.chat;

import jakarta.persistence.*;
import lombok.*;
import ru.daniil4jk.aicalendar.db.message.MessageEntity;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "chat")
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ChatEntity {
    public ChatEntity(UserEntity user) {
        this.user = user;
        user.getChats().add(this);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private final UserEntity user;

    @ToString.Exclude
    @OneToMany(mappedBy = "chat", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final List<MessageEntity> messages = new ArrayList<>();

    public void addMessage(MessageEntity message) {
        message.setChat(this);
        messages.add(message);
    }
}
