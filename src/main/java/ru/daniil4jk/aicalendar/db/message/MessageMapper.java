package ru.daniil4jk.aicalendar.db.message;

import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.ai.chat.messages.*;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "chat.id", target = "chatId")
    MessageDto toDto(MessageEntity message);

    @Mapping(source = "chatId", target = "chat.id")
    MessageEntity toEntity(MessageDto dto);

    default MessageDto toDto(@NonNull Message message, Long chatId) {
        return new MessageDto(null, message.getText(), message.getMessageType(), chatId);
    }

    default Message toAiMessage(@NonNull MessageEntity entity) {
        return switch (entity.getType()) {
            case ASSISTANT -> new AssistantMessage(entity.getText());
            case SYSTEM -> new SystemMessage(entity.getText());
            case USER -> new UserMessage(entity.getText());
            case TOOL -> throw new UnsupportedOperationException(MessageType.TOOL + " type is not supported yet");
        };
    }

    default MessageEntity toEntity(@NonNull Message message, Long chatId) {
        if (MessageType.TOOL.equals(message.getMessageType())) {
            throw new UnsupportedOperationException(MessageType.TOOL + " type is not supported yet");
        }
        return toEntity(toDto(message, chatId));
    }
}
