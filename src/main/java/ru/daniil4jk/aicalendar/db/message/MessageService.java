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
                .orElseThrow(() ->
                        new IllegalArgumentException("Message with id %s not exists"
                                .formatted(id))
                );
    }

    public MessageEntity getLastMessage(UserEntity user, Long chatId) {
        return messageRepository.findFirstByChat_IdAndChat_UserOrderByIdDesc(chatId, user)
                .orElseThrow(() ->
                        new IllegalArgumentException(("Last message for chatId %s not exists" +
                                " or not owned by user with id %s")
                                .formatted(chatId, user.getId()))
                );
    }

    public boolean exist(Long id) {
        return messageRepository.existsById(id);
    }

    //Переписать нафиг когда-нибудь, когда до сдачи будет не 11 дней, а точнее после сдачи
    //TODO заменить на Spring Expression Language проверку доступа
    private void throwIfNotExistOrNotAccess(UserEntity user, Long id) {
        if (!exist(id) ||
                !Objects.equals(
                        messageRepository.findById(id).get().getChat().getUser().getId(),
                        user.getId()
                )
        ) {
            throw new IllegalArgumentException("Message with id %s not exists"
                    .formatted(id));
        }
    }

    @Transactional
    public MessageEntity patch(UserEntity user, Long id, String newText) {
        throwIfNotExistOrNotAccess(user, id);
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
