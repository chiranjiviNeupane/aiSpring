package com.example.aiSpring;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngestController {

    private final VectorStore vectorStore;

    @Value("classpath:docs/*.md")
    private Resource[] documents;

    public IngestController (VectorStore vectorStore){
         this.vectorStore = vectorStore;
    }

    @PostMapping("/ingest")
    public String ingest() {
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(500)
                .build();

        int total = 0;
        for (Resource doc: documents){
            TextReader reader = new TextReader(doc);
            reader.getCustomMetadata().put("filename",doc.getFilename());

            List<Document> chunks = splitter.apply(reader.read());
            vectorStore.add(chunks);
            total += chunks.size();
        }
        return "Ingested " + total +" chunks from " + documents.length + " files";
    }

}
