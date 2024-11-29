package com.example.SpringAI.Controller;
import com.example.SpringAI.Services.AIServices.ConfigLangChain;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {
    private ConfigLangChain client;
//    @Autowired
//    private SejongStudentValidation sejongStudentValidation;


    @GetMapping("/call/{prompt}")
    public ResponseEntity<String> aiCall(@PathVariable String prompt){
        String response=client.chatClient().generate(prompt);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @GetMapping("/valid/{id}/{pw}")
//    public ResponseEntity<SejongAuthResponse> getValidet(@PathVariable String id,@PathVariable String pw){
//        SejongAuthResponse sejongAuthResponse= sejongStudentValidation.getValidate(id,pw);
//        return new ResponseEntity<>(sejongAuthResponse,HttpStatus.OK);
//    }
}
