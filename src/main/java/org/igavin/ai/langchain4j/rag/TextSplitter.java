package org.igavin.ai.langchain4j.rag;

import java.util.ArrayList;
import java.util.List;

public class TextSplitter {
    private static final int CHUNK_SIZE = 500; // 每个文本块的大小
    private static final int CHUNK_OVERLAP = 50; // 相邻块的重叠部分大小

    public List<String> splitText(List<String> documents) {
        List<String> chunks = new ArrayList<>();
        for (String document : documents) {
            for (int start = 0; start < document.length(); start += CHUNK_SIZE - CHUNK_OVERLAP) {
                int end = Math.min(start + CHUNK_SIZE, document.length());
                chunks.add(document.substring(start, end));
                if (end == document.length()) {
                    break;
                }
            }
        }
        return chunks;
    }
}
