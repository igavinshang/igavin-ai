package org.igavin.ai.controller;

import lombok.extern.slf4j.Slf4j;
import org.igavin.ai.chat.ChatServiceFactory;
import org.igavin.ai.config.AIProperties;
import org.igavin.ai.rag.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService ragService;

    public RagController(AIProperties aiProperties, ChatServiceFactory chatServiceFactory) {
        this.ragService = new RagService(aiProperties,chatServiceFactory);
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
            // 临时保存文件
            Path tempFile = Files.createTempFile("upload_", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());

            // 索引文件
            ragService.indexDocuments(tempFile.toString());

            // 删除临时文件
            Files.delete(tempFile);

            return ResponseEntity.ok(Map.of("message", "Document indexed successfully"));
        } catch (Exception e) {
            log.error("Error indexing document", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}