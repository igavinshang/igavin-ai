package org.igavin.ai.chat;

import org.igavin.ai.config.AIProperties;
import org.springframework.stereotype.Component;

@Component
public class ChatServiceFactory {

    private final AIProperties aiProperties;
    public ChatServiceFactory(AIProperties aiProperties) {
        this.aiProperties = aiProperties;
    }

    public ChatService getChatService() {
        switch (aiProperties.getModel().getPlatform().toLowerCase()) {
            case "ollama":
                return new OllamaChatService(aiProperties);
            case "openai":
                return new OpenAIChatService(aiProperties);
            case "aliyun":
                return new AliyunChatService(aiProperties);
            case "openrouter":
                return new OpenAIChatService(aiProperties);
            default:
                throw new IllegalArgumentException("Unsupported platform: " + aiProperties.getModel().getPlatform());
        }
    }
}
