package org.igavin.ai;

import java.io.IOException;
import java.util.List;

public interface DocumentReader {
    List<String> readDocuments(String filePath) throws IOException;
}
