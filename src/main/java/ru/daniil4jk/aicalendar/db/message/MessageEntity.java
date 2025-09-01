package ru.daniil4jk.aicalendar.db.message;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.ai.chat.messages.MessageType;
import ru.daniil4jk.aicalendar.db.chat.ChatEntity;

@Entity(name = "message")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    public MessageEntity(String text, MessageType type) {
        this.text = text;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 4128)
    private String text;
    private MessageType type;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;
}