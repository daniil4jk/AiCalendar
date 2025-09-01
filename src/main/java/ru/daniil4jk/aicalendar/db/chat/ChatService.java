package ru.daniil4jk.aicalendar.db.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.daniil4jk.aicalendar.db.message.MessageEntity;
import ru.daniil4jk.aicalendar.db.message.MessageEntityRepository;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatEntityRepository chatRepository;
    private final MessageEntityRepository messageRepository;

    public List<ChatEntity> getChats(UserEntity user) {
        return chatRepository.getAllByUser(user);
    }

    public List<MessageEntity> getMessages(UserEntity user, Long id) {
        return messageRepository.findByChat_IdAndChat_User(id, user);
    }

    public boolean exist(Long id) {
        return chatRepository.existsById(id);
    }

    public void delete(UserEntity user, Long id) {
        throwIfNotExistOrNotAccess(user, id);
        chatRepository.deleteById(id);
    }

    private void throwIfNotExistOrNotAccess(UserEntity user, Long id) {
        if (!exist(id) ||
                !Objects.equals(
                        chatRepository.findById(id).get().getUser().getId(),
                        user.getId())
        ) {
            throw getException(id);
        }
    }

    private RuntimeException getException(Long id) {
        return new IllegalArgumentException("Chat with id %s not exists"
                .formatted(id));
    }
}
