package com.example.SpringAI.Services.AIServices;

import com.example.SpringAI.Services.ServiceImpl.EmbeddingCacheServiceImpl;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;
import static java.util.stream.Collectors.joining;

@Slf4j
@Configuration
public class RAGImpl {
    @Autowired
    private ConfigLangChain configLangChain;



    public String generateRAGResponse(String resource, String prompt){

        Document document =new Document(resource);


        // Split document into segments 100 tokens each
        DocumentSplitter splitter = DocumentSplitters.recursive(
                500,
                2
        );
        List<TextSegment> segments = splitter.split(document);

        // Embed segments (convert them into vectors that represent the meaning) using embedding model
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // Store embeddings into embedding store for further search / retrieval
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);

        // Embed the question
        Embedding questionEmbedding = embeddingModel.embed(prompt).content();

        // Find relevant embeddings in embedding store by semantic similarity
        // You can play with parameters below to find a sweet spot for your specific use case
        int maxResults = 5;
        double minScore = 0.7;
        EmbeddingSearchRequest embeddingSearchRequest=EmbeddingSearchRequest.builder()
                .queryEmbedding(questionEmbedding)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
        EmbeddingSearchResult<TextSegment> relevantEmbeddings = embeddingStore.search(embeddingSearchRequest);


        // Create a prompt for the model that includes question and relevant embeddings
        PromptTemplate promptTemplate = PromptTemplate.from(
                "Generate the following requirement to the best of your ability:\n"
                        + "\n"
                        + "requirement:\n"
                        + "{{question}}\n"
                        + "\n"
                        + "Base your answer on the following information:\n"
                        + "{{information}}");
        String information = relevantEmbeddings.matches().stream()
                .map(match -> match.embedded().text())
                .collect(joining("\n\n"));

        Map<String, Object> variables = new HashMap<>();
        variables.put("question", prompt);
        variables.put("information", information);

        Prompt Modelprompt = promptTemplate.apply(variables);

        AiMessage aiMessage=configLangChain.chatClient().generate(Modelprompt.toUserMessage()).content();

        String response=aiMessage.text();

        return response;
    }

    public String generateRAGResponse2(String resource, String prompt){

        // Create a prompt for the model that includes question and relevant embeddings
//        PromptTemplate promptTemplate = PromptTemplate.from(
//                "Generate the following requirement to the best of your ability:\n"
//                        + "\n"
//                        + "requirement:\n"
//                        + "{{question}}\n"
//                        + "\n"
//                        + "Base your answer on the following information:\n"
//                        + "{{information}}");

        PromptTemplate promptTemplate = PromptTemplate.from(
                "I have a lecture slide with the following content:\n"
                        + "{{information}}\n"
                        + "\n"
                        + "Please perform the following actions to help me prepare for my exam:\n"
                        + "\n"
                        + "{{question}}\n"
                        + "\n"
                        + "Be concise and focus on the most important information for each task.");

        //                        + "1. Summarize the key points from the content above for effective revision.\n"
//                        + "2. Create a set of 3-5 multiple-choice questions (MCQs) based on the content. Ensure that each question has 4 options, and highlight the correct answer.\n"
//                        + "3. Generate 2-3 short-answer questions that could be asked in an exam based on this content.\n"

        Map<String, Object> variables = new HashMap<>();
        variables.put("question", prompt);
        variables.put("information", resource);

        Prompt Modelprompt = promptTemplate.apply(variables);

        AiMessage aiMessage=configLangChain.chatClient().generate(Modelprompt.toUserMessage()).content();

        String response=aiMessage.text();

        return response;
    }

}