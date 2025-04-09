package org.igavin.ai.langchain4j.chat;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import org.igavin.ai.config.AIProperties;

import java.util.List;

public class AliyunChatService implements ChatService {
    private final ChatLanguageModel chatModel;

    public AliyunChatService(AIProperties aiProperties){
        this.chatModel = QwenChatModel.builder()
                .apiKey(aiProperties.getModel().getApiKey())
                .modelName(aiProperties.getModel().getName())
                .build();
    }

    @Override
    public String chat(String message) {
        List<ChatMessage> chatMessages = List.of(
                new SystemMessage("You are a helpful AI assistant."),
                new UserMessage(message));

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(chatMessages)
                .build();

        return chatModel.chat(chatRequest).aiMessage().text();
    }

}
