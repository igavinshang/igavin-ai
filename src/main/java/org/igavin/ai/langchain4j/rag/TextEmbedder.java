package org.igavin.ai.langchain4j.rag;

import com.alibaba.dashscope.embeddings.TextEmbedding;
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

    public TextEmbedder(String apiKey, String baseUrl, String modelName,String platform) {
        if (platform.equals("ollama")) {
            this.embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl(baseUrl)
                    .modelName(modelName)
                    .build();
        } else if (platform.equals("aliyun")) {
            this.embeddingModel = QwenEmbeddingModel.builder()
                    .apiKey(apiKey)
                    .modelName(TextEmbedding.Models.TEXT_EMBEDDING_V1)
                    .build();
         } else {
            this.embeddingModel = OpenAiEmbeddingModel.builder()
                    .apiKey(apiKey)
                    .baseUrl(baseUrl)
                    .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL) //支持嵌入生成的模型
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
