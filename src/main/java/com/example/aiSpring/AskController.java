package com.example.aiSpring;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {
    private final ChatClient chatClient;

    public AskController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Value("classpath:/prompts/qa.st")
    private Resource qaPrompt;

    public record AskRequest(String question) {    }
    public record AskResponse(String answer) {    }

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest askRequest) {
        String answer = chatClient.prompt()
                .user(u->u.text(qaPrompt).param("question",askRequest.question()))
                .call()
                .content();
        return new AskResponse(answer);
    }
}
