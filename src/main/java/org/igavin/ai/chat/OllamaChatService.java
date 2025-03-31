package org.igavin.ai.chat;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.igavin.ai.config.AIProperties;;

public class OllamaChatService implements ChatService {
    private final ChatLanguageModel chatModel;

    public OllamaChatService(AIProperties aiProperties){
        this.chatModel = OllamaChatModel.builder()
                .baseUrl(aiProperties.getModel().getBaseUrl())
                .modelName(aiProperties.getModel().getName())
                .build();
    }

    @Override
    public String chat(String message) {
        return chatModel.generate(
                new SystemMessage("You are a helpful AI assistant."),
                new UserMessage(message)).content().text();
    }
}
