package com.example.SpringAI.Services.AIServices;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface Assistant {
    String chat(@MemoryId String memoryId, @UserMessage dev.langchain4j.data.message.UserMessage userMessage);
}