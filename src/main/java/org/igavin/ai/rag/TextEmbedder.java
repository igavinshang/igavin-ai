package org.igavin.ai.rag;

import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;

import java.util.ArrayList;
import java.util.List;

public class TextEmbedder {
    private final EmbeddingModel embeddingModel;

    public TextEmbedder(String apiKey, String baseUrl, String modelName) {
        if (baseUrl.contains("localhost") || baseUrl.contains("ollama")) {
            this.embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl(baseUrl)
                    .modelName(modelName)
                    .build();
        } else if (baseUrl.contains("dashscope") || baseUrl.contains("aliyun")) {
            this.embeddingModel = QwenEmbeddingModel.builder()
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .build();
        } else {
            this.embeddingModel = OpenAiEmbeddingModel.builder()
                    .apiKey(apiKey)
                    .baseUrl(baseUrl)
                    .modelName(modelName)
                    .build();
        }
    }

    public List<Embedding> embedTexts(List<String> texts) {
        List<Embedding> embeddings = new ArrayList<>();
        for (String text : texts) {
            embeddings.add(
                    embeddingModel.embed(text).content());
        }
        return embeddings;
    }
}
