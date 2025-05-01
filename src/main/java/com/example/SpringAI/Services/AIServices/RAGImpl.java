package com.example.SpringAI.Services.AIServices;
import com.example.SpringAI.Utility.GenerateQuestions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentByRegexSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Slf4j
@Configuration
public class RAGImpl {
    @Autowired
    private ConfigLangChain configLangChain;


    public String generateRAGResponse(String resource, String prompt) {
        Document document = new Document(resource);
        log.info("Raw tex: "+document.toString());
        // Split document into segments 1000 tokens each
        DocumentSplitter splitter = new DocumentByParagraphSplitter(500,3);
        List<TextSegment> segments = splitter.split(document);
        log.info("Segment Size: "+segments.size());
        log.info("Segmented : "+segments.toString());

        // Embed segments (convert them into vectors that represent the meaning) using embedding model
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);
        // Embed the question
        String information = null;
        //cleaning user question
        List<String> questions = configLangChain.simplifyQuestions(prompt);
        for (int i = 0; i < questions.size(); i++) {
            //embedding an AI question
            Embedding questionEmbedding = embeddingModel.embed(questions.get(i)).content();
            // Find relevant embeddings in embedding store by semantic similarity
            // You can play with parameters below to find a sweet spot for your specific use case
            int maxResults = 3;
            double minScore = 0.3;
            //setting perameters for sementic search
            EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(questionEmbedding)
                    .maxResults(maxResults)
                    .minScore(minScore)
                    .build();
            EmbeddingSearchResult<TextSegment> relevantEmbeddings = embeddingStore.search(embeddingSearchRequest);

            information += relevantEmbeddings.matches().stream()
                    .map(match -> match.embedded().text())
                    .collect(joining("\n\n"));
        }


        // Create a prompt for the model that includes question and relevant embeddings
        PromptTemplate promptTemplate = PromptTemplate.from(
                "Generate the following requirement to the best of your ability:\n"
                        + "\n"
                        + "requirement:\n"
                        + "{{question}}\n"
                        + "\n"
                        + "Base your answer on the following content:\n"
                        + "{{information}}");


        log.info("INFORMATION:" + information);
        log.info("Size of the information: "+information.length());
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", prompt);
        variables.put("information", information);
        Prompt Modelprompt = promptTemplate.apply(variables);


        log.info("Final prompt: "+Modelprompt.text());
        log.info("Size of final prompt: "+Modelprompt.toString().length());




        AiMessage aiMessage = configLangChain.chatClient().generate(Modelprompt.toUserMessage()).content();


        String response = aiMessage.text();

    return response;

    }






    public String generateRAGResponse2(String resource, String prompt) {

        Document document = new Document(resource);
        // Split document into segments
        DocumentSplitter splitter = new DocumentByRegexSplitter("\n\n\n", " ", 5000, 05);
        List<TextSegment> segments = splitter.split(document);
        List<String> text = segments.stream()
                .map(TextSegment::text)  // Extract the text from each TextSegment
                .collect(Collectors.toUnmodifiableList());

        String response = pageSummarization(text);

//Second
        PromptTemplate promptTemplate2 = PromptTemplate.from(
                "Complete the following task to the best of your ability:\n"
                        + "\n"
                        + "task:\n"
                        + "{{question2}}\n"
                        + "\n"
                        + "complete the task Based on the following content:\n"
                        + "{{information2}}");

        Map<String, Object> variables2 = new HashMap<>();
        variables2.put("question2", prompt);
        variables2.put("information2", response);
        Prompt Modelprompt2 = promptTemplate2.apply(variables2);

        AiMessage aiMessage2 = configLangChain.chatClient().generate(Modelprompt2.toUserMessage()).content();


        return aiMessage2.text();
    }


    public String pageSummarization(List<String> text) {
        // Use a thread-safe collection to accumulate responses
        List<String> responses = Collections.synchronizedList(new ArrayList<>());

        // Create a thread pool with a fixed number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(text.size(), 10)); // Limit max threads to prevent overhead

        List<Future<?>> futures = new ArrayList<>();
        for (String str : text) {
            futures.add(executorService.submit(() -> {
                try {
                    PromptTemplate promptTemplate = PromptTemplate.from(
                            "This is a page of a lecture slide with the following content:\n"
                                    + "{{information}}\n"
                                    + "\n"
                                    + "Please perform the following actions to help me prepare for my exam:\n"
                                    + "\n"
                                    + "{{question}}\n"
                                    + "\n"
                                    + "Be concise and focus on the most important information for each task.");

                    String action = """ 
                    Please summarize the page, providing:
                    1. Key Topics: The main subject or theme of the slide.
                    2. Key Points: Bullet points summarizing the essential information or concepts.
                    3. Important Terms/Definitions: Any important terms or definitions mentioned in the slide.
                    The summary should retain the core meaning and exclude examples, excessive details, or redundant information. Format the output clearly and ensure it is suitable for quick revision before an exam.
                    """;

                    Map<String, Object> variables = new HashMap<>();
                    variables.put("question", action);
                    variables.put("information", str);

                    Prompt Modelprompt = promptTemplate.apply(variables);
                    AiMessage aiMessage = configLangChain.chatClient().generate(Modelprompt.toUserMessage()).content();

                    // Add the result to the thread-safe list
                    responses.add(aiMessage.text());
                } catch (Exception e) {
                    System.err.println("Task failed: " + e.getMessage());
                }
            }));
        }

        // Wait for all tasks to complete

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error while waiting for task completion: " + e.getMessage());
            }
        }

        // Shut down the executor service
        executorService.shutdown();

        // Combine the responses into a single string
        return String.join("\n", responses);
    }


}




