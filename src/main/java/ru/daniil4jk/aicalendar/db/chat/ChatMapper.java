package ru.daniil4jk.aicalendar.db.chat;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatId toId(ChatEntity entity);
}
