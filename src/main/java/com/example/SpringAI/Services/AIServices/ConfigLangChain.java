package com.example.SpringAI.Services.AIServices;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Configuration
@Slf4j
public class ConfigLangChain {
         private String MODEL_NAME = "llama3.2";
         private String Base_URL="http://localhost:11434/";

         @Bean
         public ChatLanguageModel chatClient(){
             ChatLanguageModel model= OllamaChatModel.builder()
                     .baseUrl(Base_URL)
                     .modelName(MODEL_NAME)
                     .timeout(Duration.ofMinutes(5))
                     .topK(10)
                     .topP(.5)
                     .build();
             return model;
         }

}
