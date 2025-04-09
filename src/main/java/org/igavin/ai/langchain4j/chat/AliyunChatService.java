package org.igavin.ai.langchain4j.chat;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
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

        // ToolSpecification toolSpecification = ToolSpecification.builder()
        // .name("getWeatherServiceFunction")
        // .description("Use api.weather to get weather information.")
        // .parameters(JsonObjectSchema.builder()
        // .addStringProperty("city", "The city for which the weather forecast should be
        // returned")
        // .required("city") // the required properties should be specified explicitly
        // .build())
        // .build();

        List<ChatMessage> chatMessages = List.of(
                new SystemMessage("You are a helpful AI assistant."),
                new UserMessage(message));
        // ChatRequest chatRequest = ChatRequest.builder()
        // // .toolSpecifications(toolSpecification)
        // .messages(chatMessages)
        // .build();

        return chatModel.generate(chatMessages).content().text().toString();
    }
}
