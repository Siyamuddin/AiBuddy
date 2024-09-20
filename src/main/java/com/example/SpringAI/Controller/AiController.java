package com.example.SpringAI.Controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {
    private final OllamaChatModel client;

    public AiController(OllamaChatModel client) {
        this.client = client;
    }

    @GetMapping("/call/{prompt}")
    public Flux<String> aiCall(@PathVariable String prompt){
        return client.stream(prompt);
    }
}
