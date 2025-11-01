package ru.daniil4jk.aicalendar.web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.daniil4jk.aicalendar.ai.AiService;
import ru.daniil4jk.aicalendar.ai.dto.AiOutput;
import ru.daniil4jk.aicalendar.db.chat.ChatId;
import ru.daniil4jk.aicalendar.db.chat.ChatMapper;
import ru.daniil4jk.aicalendar.db.chat.ChatService;
import ru.daniil4jk.aicalendar.db.message.MessageDto;
import ru.daniil4jk.aicalendar.db.message.MessageMapper;
import ru.daniil4jk.aicalendar.db.message.MessageService;
import ru.daniil4jk.aicalendar.web.security.user.UserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class AiChatController {
    private final AiService aiService;
    private final ChatService chatService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final ChatMapper chatMapper;

    @GetMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<ChatId>> getChats(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                chatService.getChats(user.getInherit())
                        .stream()
                        .map(chatMapper::toId)
                        .toList()
        );
    }

    //Переписать нафиг когда-нибудь, когда до сдачи будет не 11 дней, а точнее после сдачи
    //TODO переместить в messageRepository
    @GetMapping("/{chatId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<MessageDto>> getChatHistory(@AuthenticationPrincipal UserPrincipal user,
                                                    @PathVariable Long chatId) {
        return ResponseEntity.ok(
                chatService.getMessages(user.getInherit(), chatId)
                        .stream()
                        .map(messageMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/{chatId}/message/last")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<MessageDto> getLastMessage(@AuthenticationPrincipal UserPrincipal user,
                                                     @PathVariable Long chatId) {
        return ResponseEntity.ok(
                messageMapper.toDto(
                        messageService.getLastMessage(user.getInherit(), chatId)
                )
        );
    }

    @PostMapping("/new/message")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<AiOutput> postMessageToNewChat(@AuthenticationPrincipal UserPrincipal user,
                                                         @RequestBody MessageText message) {
        return postMessage(user, null, message);
    }

    @PostMapping("/{chatId}/message")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<AiOutput> postMessage(@AuthenticationPrincipal UserPrincipal user,
                                                @PathVariable(required = false) Long chatId,
                                                @RequestBody MessageText message) {
        return ResponseEntity.ok(
                aiService.sendMessage(
                        user.getInherit(),
                        new MessageDto(null, message.text(), MessageType.USER, chatId)
                )
        );
    }

    @PatchMapping("/message/{messageId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<MessageDto> patchMessage(@AuthenticationPrincipal UserPrincipal user,
                                                   @PathVariable Long messageId,
                                                   @RequestBody MessageText message) {
        return ResponseEntity.ok(messageMapper.toDto(
                messageService.patch(user.getInherit(), messageId, message.text())
        ));
    }

    @DeleteMapping("/message/{messageId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Object> deleteMessage(@AuthenticationPrincipal UserPrincipal user,
                                                @PathVariable Long messageId) {
        messageService.delete(user.getInherit(), messageId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> deleteChat(@AuthenticationPrincipal UserPrincipal user,
                                        @PathVariable Long chatId) {
        chatService.delete(user.getInherit(), chatId);
        return ResponseEntity.ok().build();
    }



    public record MessageText(String text) {}
}
