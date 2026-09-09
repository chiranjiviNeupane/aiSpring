package com.example.aiSpring.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolController {

    private final ChatClient chatClient;
    private final DemoTools demoTools;
    private final OrderTools orderTools;

    public ToolController(ChatClient.Builder builder, DemoTools demoTools, OrderTools orderTools) {
        this.chatClient = builder.build();
        this.demoTools = demoTools;
        this.orderTools = orderTools;
    }

    @PostMapping("/ask-tools")
    public String askTools(@RequestBody String question) {
        return chatClient.prompt()
                .tools(demoTools,orderTools)
                .user(question)
                .call()
                .content();
    }
}
