package org.igavin.ai.langchain4j.rag;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TextFileDocumentReader implements DocumentReader {
    @Override
    public List<String> readDocuments(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
    }
}
