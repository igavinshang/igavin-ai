package org.igavin;

import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;

import java.util.ArrayList;
import java.util.List;

public class TextEmbedder {
    private final EmbeddingModel embeddingModel;

    public TextEmbedder(String apiKey,String baseUrl,String modelName) {
        this.embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(baseUrl)
                //.apiKey(apiKey)
                .modelName(modelName)
                .build();
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
