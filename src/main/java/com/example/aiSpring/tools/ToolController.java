package com.example.aiSpring.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolController {

    private final ChatClient chatClient;
    private final DemoTools demoTools;

    public ToolController(ChatClient.Builder builder, DemoTools demoTools) {
        this.chatClient = builder.build();
        this.demoTools = demoTools;
    }

    @PostMapping("/ask-tools")
    public String askTools(@RequestBody String question) {
        return chatClient.prompt()
                .tools(demoTools)
                .user(question)
                .call()
                .content();
    }
}
