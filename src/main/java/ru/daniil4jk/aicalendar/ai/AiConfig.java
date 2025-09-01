package ru.daniil4jk.aicalendar.ai;

import chat.giga.springai.GigaChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import ru.daniil4jk.aicalendar.ai.function.EventFunctions;
import ru.daniil4jk.aicalendar.ai.memory.SavingChatMemoryWrapper;

import java.util.Objects;

@Slf4j
@Configuration
public class AiConfig {
    public static final String SYS_PROMPT_FILE_NAME = "/system_prompt.txt";
    private final Resource systemPrompt = new InputStreamResource(
            Objects.requireNonNull(this.getClass().getResourceAsStream(SYS_PROMPT_FILE_NAME))
    );

    @Bean
    public ChatClient client(GigaChatModel model, EventFunctions functions, SavingChatMemoryWrapper memory) {
        return ChatClient.builder(model)
                .defaultSystem(systemPrompt)
                .defaultTools(functions)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }
}
