package ru.daniil4jk.aicalendar.ai;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.daniil4jk.aicalendar.ai.dto.AiOutput;
import ru.daniil4jk.aicalendar.ai.function.EventFunctions;
import ru.daniil4jk.aicalendar.ai.memory.SavingChatMemoryWrapper;
import ru.daniil4jk.aicalendar.db.chat.ChatEntity;
import ru.daniil4jk.aicalendar.db.chat.ChatEntityRepository;
import ru.daniil4jk.aicalendar.db.message.MessageDto;
import ru.daniil4jk.aicalendar.db.message.MessageMapper;
import ru.daniil4jk.aicalendar.db.user.UserEntity;
import ru.daniil4jk.aicalendar.db.user.UserEntityRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {
    private final MessageMapper mapper;
    private final ChatEntityRepository chatRepository;
    private final UserEntityRepository userRepository;
    private final AiCaller caller;

    private final EventFunctions functions;
    private final SavingChatMemoryWrapper memory;

    @Transactional
    public AiOutput sendMessage(UserEntity user, MessageDto input) {
        setChatIfAbsent(user, input);
        caller.processInput(input);
        return new AiOutput(
                mapper.toDto(memory.getRequest()),
                mapper.toDto(memory.getResponse()),
                functions.getEvents()
        );
    }

    private void setChatIfAbsent(UserEntity user, MessageDto input) {
        if (input.getChatId() != null) {
            ChatEntity chat = chatRepository.findById(input.getChatId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Chat %s was not found".formatted(input.getChatId())
                    ));
            controlChatAccess(user, chat);
        } else {
            ChatEntity chat = createChat(user);
            chat = chatRepository.save(chat);
            input.setChatId(chat.getId());
        }
    }

    private ChatEntity createChat(UserEntity user) {
        return new ChatEntity(
                userRepository.findById(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "User %s not exist".formatted(user)
                        ))
                );
    }

    private void controlChatAccess(UserEntity user, ChatEntity chat) {
        if (!chat.getUser().getId().equals(user.getId())) {
            throw new SecurityException("user %s has`nt access to chat %s".formatted(user, chat));
        }
    }


}

