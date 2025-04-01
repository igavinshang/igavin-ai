package org.igavin.ai.langchain4j.rag;

import java.io.IOException;
import java.util.List;

public interface DocumentReader {
    List<String> readDocuments(String filePath) throws IOException;
}
