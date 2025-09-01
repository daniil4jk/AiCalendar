package ru.daniil4jk.aicalendar.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;
import ru.daniil4jk.aicalendar.db.message.MessageDto;

@Component
@RequiredArgsConstructor
public class AiCaller {
    private final ChatClient client;

    public void processInput(MessageDto input) {
        try {
            client.prompt(input.getText())
                    .advisors(advisorSpec ->
                            advisorSpec.param(ChatMemory.CONVERSATION_ID, input.getChatId())
                    )
                    .call()
                    .chatResponse();
        } catch (Exception e) {
            throw new RuntimeException(e); //todo понять что за ошибка и нормально обработать
        }
    }
}
