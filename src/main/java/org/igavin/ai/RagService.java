package org.igavin.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;


import java.util.List;
import java.util.stream.Collectors;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import lombok.extern.slf4j.Slf4j;
import org.igavin.TextEmbedder;

@Slf4j
public class RagService {
    private final TextEmbedder textEmbedder;
    private final VectorStore vectorStore;
    private final ChatLanguageModel chatModel;
    private static final int MAX_RESULTS = 15;

    public RagService(String modelApiKey, String modelUrl, String modelName,String esHost,String esApiKey, String indexName) {
        this.textEmbedder = new TextEmbedder(modelApiKey,modelUrl,modelName);
        this.vectorStore = new VectorStore(esHost, esApiKey, indexName);
        this.chatModel = OllamaChatModel.builder()
                .baseUrl(modelUrl)
                //.apiKey(modelApiKey)
                .modelName(modelName)
                .build();
    }

    public String generateResponse(String userQuery) {
        // 将用户查询转换为向量
        Embedding queryEmbedding = textEmbedder.embedTexts(List.of(userQuery)).get(0);

        // 从向量存储中检索相关文档
        List<EmbeddingMatch<TextSegment>> relevantEmbeddings = vectorStore.searchSimilarEmbeddings(queryEmbedding,
                MAX_RESULTS);

        if (relevantEmbeddings.isEmpty()) {
            return "I couldn't find any relevant information to answer your question.";
        }

        // 提取检索到的文本
        String relevantDocs = relevantEmbeddings.stream()
                .map(EmbeddingMatch::embedded)
                .map(TextSegment::text)
                .collect(Collectors.joining("\n\n"));

        // 构建提示
        String prompt = "Based on the following information:\n\n" + relevantDocs +
                "\n\nPlease answer this question: " + userQuery;

        // 使用LLM生成回答
        return chatModel.generate(
                new SystemMessage(
                        "You are a helpful assistant that answers questions based only on the provided information."),
                new UserMessage(prompt)).toString();
    }

    // 添加一个方法来索引新文档
    public void indexDocuments(String filePath) {
        try {
            // 读取文档
            DocumentReader reader = new TextFileDocumentReader();
            List<String> documents = reader.readDocuments(filePath);

            // 分割文本
            TextSplitter splitter = new TextSplitter();
            List<String> chunks = splitter.splitText(documents);

            // 为分割后的文本创建嵌入向量
            List<Embedding> embeddings = textEmbedder.embedTexts(chunks);

            // 存储嵌入向量
            vectorStore.storeEmbeddings(embeddings, chunks);

        } catch (Exception e) {
            log.error("Error indexing documents", e);
            throw new RuntimeException("Failed to index documents", e);
        }
    }
}
