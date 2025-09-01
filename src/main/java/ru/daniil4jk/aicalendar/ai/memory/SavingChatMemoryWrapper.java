package ru.daniil4jk.aicalendar.ai.memory;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.daniil4jk.aicalendar.db.DbChatMemory;
import ru.daniil4jk.aicalendar.db.message.MessageEntity;

import java.util.List;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@RequiredArgsConstructor
public class SavingChatMemoryWrapper implements ChatMemory {
    private final DbChatMemory dbMemory;
    @Getter
    private MessageEntity request;
    @Getter
    private MessageEntity response;

    @Override
    public void add(@NonNull String conversationId, @NonNull List<Message> messages) {
        List<MessageEntity> saved = dbMemory.addAndReturn(conversationId, messages);
        for (MessageEntity message : saved) {
            if (MessageType.USER.equals(message.getType())) {
                setRequest(message);
            } else if (MessageType.ASSISTANT.equals(message.getType())) {
                setResponse(message);
            }
        }
    }

    @Override
    @NonNull
    public List<Message> get(@NonNull String conversationId) {
        return dbMemory.get(conversationId);
    }

    @Override
    public void clear(@NonNull String conversationId) {
        dbMemory.clear(conversationId);
    }

    public void setRequest(MessageEntity request) {
        if (this.request != null) throw new UnsupportedOperationException("more than one request " + request);
        this.request = request;
    }

    public void setResponse(MessageEntity response) {
        if (this.response != null) throw new UnsupportedOperationException("more than one response " + response);
        this.response = response;
    }
}
