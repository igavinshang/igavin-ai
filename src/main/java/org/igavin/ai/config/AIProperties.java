package org.igavin.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "igavin.ai", ignoreInvalidFields = true)
public class AIProperties {

    private Model model;
    private Elasticsearch elasticsearch;

    @Data
    public static class Model {
        private String platform;
        private String apiKey;
        private String baseUrl;
        private String name;
    }

    @Data
    public static class Elasticsearch {
        private String apiKey;
        private String url;
        private String indexName;
    }

}
