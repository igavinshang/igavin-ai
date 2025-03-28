package org.igavin.ai;


import co.elastic.clients.util.ApiTypeHelper;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import java.util.List;

public class VectorStore {
    private final EmbeddingStore<TextSegment> embeddingStore;

    public VectorStore(String host, String apiKey, String indexName) {
        this.embeddingStore = ElasticsearchEmbeddingStore.builder()
                .serverUrl("http://" + host)
//                .restClient(RestClient.builder(
//                        new HttpHost(host)
//                        )
////                        .setDefaultHeaders(
////                                new Header[] {
////                                        new BasicHeader("Authorization", "ApiKey " + apiKey),
////                                })
//                        .build())
                .indexName(indexName)
                .build();
    }

    public void storeEmbeddings(List<Embedding> embeddings, List<String> texts) {
        for (int i = 0; i < embeddings.size(); i++) {
            embeddingStore.add(
                    embeddings.get(i),
                    TextSegment.from(texts.get(i))
            );
        }
    }

    public List<EmbeddingMatch<TextSegment>> searchSimilarEmbeddings(Embedding queryEmbedding, int maxResults) {
        ApiTypeHelper.DANGEROUS_disableRequiredPropertiesCheck(false);
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .minScore(0.8) // Minimal similarity threshold
                .build();
        return embeddingStore.search(request).matches();

    }
}
