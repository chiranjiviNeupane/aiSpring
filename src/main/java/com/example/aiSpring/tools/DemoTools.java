package com.example.aiSpring.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DemoTools {

    @Tool(description = "Get the current server date and time in ISO 8601 format")
    public String getCurrentDateTime() {
        System.out.println(">>> TOOL CALLED: getCurrentDateTime");
        return LocalDateTime.now().toString();
    }
}
