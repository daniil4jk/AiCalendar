package ru.daniil4jk.aicalendar.db.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.ai.chat.messages.MessageType;

@Data
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String text;
    private MessageType type;
    private Long chatId;
}
