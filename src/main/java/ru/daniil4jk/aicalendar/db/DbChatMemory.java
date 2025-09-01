package ru.daniil4jk.aicalendar.db;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;
import ru.daniil4jk.aicalendar.db.chat.ChatEntity;
import ru.daniil4jk.aicalendar.db.chat.ChatEntityRepository;
import ru.daniil4jk.aicalendar.db.message.MessageEntity;
import ru.daniil4jk.aicalendar.db.message.MessageEntityRepository;
import ru.daniil4jk.aicalendar.db.message.MessageMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DbChatMemory implements ChatMemory {
    private final ChatEntityRepository chatRepository;
    private final MessageEntityRepository messageRepository;
    private final MessageMapper mapper;

    @Override
    @Transactional
    public void add(@NonNull String conversationId, @NonNull List<Message> messages) {
        addAndReturn(conversationId, messages);
    }

    @Transactional
    public List<MessageEntity> addAndReturn(@NonNull String conversationId, @NonNull List<Message> messages) {
        Optional<ChatEntity> chatOptional = chatRepository.findById(Long.parseLong(conversationId));

        ChatEntity chat = chatOptional.orElseThrow(() ->
                new IllegalArgumentException("chat with id %s not exist".formatted(conversationId)));

        List<MessageEntity> entities = messages.stream()
                .map(message -> mapper.toEntity(message, chat.getId()))
                .peek(chat::addMessage)
                .toList();

        return messageRepository.saveAll(entities);
    }

    @Override
    @NonNull
    @Transactional
    public List<Message> get(@NonNull String conversationId) {
        Optional<ChatEntity> chat = chatRepository.findById(Long.parseLong(conversationId));
        return chat.map(chatEntity -> chatEntity
                        .getMessages().stream()
                        .map(mapper::toAiMessage)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public void clear(@NonNull String conversationId) {
        throw new RuntimeException("not implemented");
    }
}
