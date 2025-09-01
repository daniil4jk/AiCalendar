package ru.daniil4jk.aicalendar.db.message;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageEntityRepository messageRepository;

    public MessageEntity getMessage(UserEntity user, Long id) {
        return messageRepository.findByIdAndChat_User(id, user)
                .orElseThrow(() -> getException(id));
    }

    public boolean exist(Long id) {
        return messageRepository.existsById(id);
    }

    private void throwIfNotExistOrNotAccess(UserEntity user, Long id) {
        if (!exist(id) ||
                !Objects.equals(
                        messageRepository.findById(id).get().getChat().getUser().getId(),
                        user.getId())
        ) {
            throw getException(id);
        }
    }

    private RuntimeException getException(Long id) {
        return new IllegalArgumentException("Message with id %s not exists"
                .formatted(id));
    }

    @Transactional
    public MessageEntity patch(UserEntity user, Long id, String newText) {
        MessageEntity message = getMessage(user, id);
        message.setText(newText);
        messageRepository.save(message);
        return message;
    }

    public void delete(UserEntity user, Long id) {
        throwIfNotExistOrNotAccess(user, id);
        messageRepository.deleteById(id);
    }
}
