package com.example.SpringAI.Services.AIServices;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.mistralai.MistralAiStreamingChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofSeconds;

@Configuration
@Slf4j
public class ConfigLangChain {

    @Value("${ai.model.name}")
    private String MODEL_NAME;
    @Value("${ai.model.name.embeddig}")
    private String EMBEDDING_MODEL_NAME;
    @Value("${ai.base.url}")
    private String Base_URL;
    @Value("${ai.api.key}")
    private String API_KEY;
    @Value("${embd.api.key}")
    private String EMBD_API_KEY;

    @Bean
    public ChatLanguageModel chatClient(){
        ChatLanguageModel model= OpenAiChatModel.builder()
                .baseUrl(Base_URL)
                .apiKey(API_KEY)
                .modelName(MODEL_NAME)
                .temperature(0.0)
                .maxTokens(1024)
                .build();
        return  model;
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        EmbeddingModel embeddingModel = HuggingFaceEmbeddingModel.builder()
                .accessToken(EMBD_API_KEY)
                .modelId(EMBEDDING_MODEL_NAME)
                .build();
        return  embeddingModel;
    }



    public static List<String> splitQuestions(String text) {
        List<String> questions = new ArrayList<>();

        // Split the string based on punctuation followed by a space and an uppercase letter
        String[] splitQuestions = text.split("(?<=\\?)\\s+(?=[A-Z])");

        for (String question : splitQuestions) {
            questions.add(question.trim()); // Trim each question to remove any extra spaces
        }
        log.info("Generated questions: "+questions);
        return questions;
    }



    public List<String> simplifyQuestions(String userQuerry){
            String prompt="You are an AI language model assistant." +
                    " Your task is to generate five different versions of the given user question to retrieve relevant documents from a vector database." +
                    " By generating multiple perspectives on the user question, your goal is to help the user overcome some of the limitations of the distance-based similarity search.\n" +
                    "Provide these alternative questions separated by newlines, Original question:"+userQuerry+"?";
            String response=chatClient().generate(prompt);

            List<String> ls=splitQuestions(response);
            return ls;
    }

}
