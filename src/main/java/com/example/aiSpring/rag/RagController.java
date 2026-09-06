package com.example.aiSpring.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagController(ChatClient.Builder builder, VectorStore vectorStore){
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    // search the local vector embedding, then prompt gemini with embedding data
    @PostMapping("/ask-docs")
    public String askDocs(@RequestBody String question){
        var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.45)
                        .topK(4)
                        .build())
                .build();

        return chatClient.prompt()
                .advisors(qaAdvisor)
                .user(question)
                .call()
                .content();
    }

    //only for testing to see the difference between local embedding response and /ask-docs
    //we will be using this search endpoint to test 5 expected prompt and 5 unexpected prompt
    //with the result we will use the avg score as threshold value for other end-points
    @PostMapping("/search")
    public List<String> search(@RequestBody String question){
        List<Document> hits = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .similarityThreshold(0.0)
                        .topK(4)
                        .build());

        return hits.stream()
                .map(d -> "score=" + d.getScore()
                + "file=" + d.getMetadata().get("filename")
                + " :: " + d.getText().substring(0,Math.min(120,d.getText().length())))
                .toList();
    }

    public record GroundedAnswer(String answer, List<String> sourceFiles, boolean answeredFromContext) {}

    //same as /ask-docs but with structured output
    @PostMapping("/ask-structured")
    public GroundedAnswer askStructured(@RequestBody String question){
        var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.45)
                        .topK(4)
                        .build())
                .build();

        return chatClient.prompt()
                .advisors(qaAdvisor)
                .user(question)
                .call()
                .entity(GroundedAnswer.class);
    }
}
