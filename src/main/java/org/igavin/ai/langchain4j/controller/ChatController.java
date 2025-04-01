package org.igavin.ai.langchain4j.controller;

import lombok.extern.slf4j.Slf4j;
import org.igavin.ai.langchain4j.chat.ChatService;
import org.igavin.ai.langchain4j.chat.ChatServiceFactory;
import org.igavin.ai.config.AIProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatServiceFactory chatServiceFactory;
    private final AIProperties aiProperties;

    public ChatController(ChatServiceFactory chatServiceFactory,
            AIProperties aiProperties){
        this.chatServiceFactory = chatServiceFactory;
        this.aiProperties = aiProperties;
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Message is required"));
        }

        try {
            ChatService chatService = chatServiceFactory.getChatService();
            String response = chatService.chat(message);

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("response", response);
            responseBody.put("platform", aiProperties.getModel().getPlatform());

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("api chat error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
