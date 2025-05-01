package com.example.SpringAI.Services.AIServices;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.ArrayList;
import java.util.List;


@Configuration
@Slf4j
public class ConfigLangChain {

    @Value("${ai.model.name}")
    private String MODEL_NAME;
    @Value("${ai.base.url}")
    private String Base_URL;
    @Value("${ai.api.key}")
    private String API_KEY;
//configured ami model
    @Bean
    public ChatLanguageModel chatClient(){
        return  OpenAiChatModel.builder()
                .baseUrl(Base_URL)
                .apiKey(API_KEY)
                .modelName(MODEL_NAME)
                .temperature(0.0)
                .maxTokens(1024)
                .build();

    }

    // splitting AI generated sample questions
    public static List<String> splitQuestions(String text) {
        List<String> questions = new ArrayList<>();

        // Split the string based on punctuation followed by a space and an uppercase letter
        String[] splitQuestions = text.split("(?<=\\?)\\s+(?=[A-Z])");

        for (String question : splitQuestions) {
            questions.add(question.trim()); // Trim each question to remove any extra spaces
        }

        return questions;
    }


//Generate 5 questions based on the user questions.
    public List<String> simplifyQuestions(String userQuerry){
        String prompt="You are an AI language model assistant." +
                " Your task is to generate five different versions of the given user question to retrieve relevant documents from a vector database." +
                " By generating multiple perspectives on the user question, your goal is to help the user overcome some of the limitations of the distance-based similarity search.\n" +
                "Provide these alternative questions separated by newlines, Original question:"+userQuerry+"?";
        String response=chatClient().generate(prompt);

        return splitQuestions(response);

    }

}