package org.igavin.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class RagController {

    private final RagService ragService;

    public RagController(
            @Value("${model.api.key}") String modelApiKey,
            @Value("${model.url}") String modelUrl,
            @Value("${model.name}") String modelName,
            @Value("${elasticsearch.url}") String esHost,
            @Value("${elasticsearch.api-key}") String esApiKey,
            @Value("${elasticsearch.index-name}") String indexName) {
        this.ragService = new RagService(modelApiKey,modelUrl,modelName, esHost, esApiKey, indexName);
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, String>> query(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Query is required"));
        }

        String response = ragService.generateResponse(query);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("response", response);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/index")
    public ResponseEntity<?> indexFile(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file temporarily
            Path tempFile = Files.createTempFile("upload_", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());

            // Index the file
            ragService.indexDocuments(tempFile.toString());

            // Delete the temp file
            Files.delete(tempFile);

            return ResponseEntity.ok(Map.of("message", "Document indexed successfully"));
        } catch (Exception e) {
            log.error("Error indexing document", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}