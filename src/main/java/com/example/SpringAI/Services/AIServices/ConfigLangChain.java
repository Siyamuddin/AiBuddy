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

         @Bean
         public ChatLanguageModel chatClient(){
                     .baseUrl(Base_URL)
                     .modelName(MODEL_NAME)
                     .build();
             return  model;
         }

}
