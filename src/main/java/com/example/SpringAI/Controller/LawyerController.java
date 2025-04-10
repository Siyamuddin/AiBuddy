package com.example.SpringAI.Controller;


import com.example.SpringAI.Services.AIServices.RAGImpl;
import com.example.SpringAI.Services.LawyerServices;
import com.example.SpringAI.Services.ServiceImpl.FileService;
import com.example.SpringAI.Utility.APIResponse;
import com.example.SpringAI.Utility.ChatRequest;
import com.example.SpringAI.Utility.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/lawyer")
public class LawyerController {
    private LawyerServices lawyerServices;
    public LawyerController(LawyerServices lawyerServices) {
        this.lawyerServices = lawyerServices;
    }
    @Autowired
    private RAGImpl rag;
    @Autowired
    private FileService fileService;

    @PostMapping("/upload/dataset/{userid}")
    public ResponseEntity<APIResponse> uploadDataset(@PathVariable Long userid, MultipartFile file) throws IOException {
        APIResponse apiResponse=lawyerServices.uploadDataSet(userid,file);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/triger/embedding")
    public ResponseEntity<APIResponse> trigerEmbedding() throws FileNotFoundException {
        InputStream inputStream=fileService.getSource("file/","DATASET.pdf");
        rag.cleanAndStoreData(inputStream);
        APIResponse apiResponse=new APIResponse("Embedding Completed",true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/chat")
    public ResponseEntity<ChatResponse> chatLawyer(@RequestBody ChatRequest chatRequest) {

        ChatResponse resp=lawyerServices.chatLawyer(chatRequest);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }
    @GetMapping("/chat/{userid}/{message}")
    public ResponseEntity<ChatResponse> chatLawyer2(@PathVariable Long userid, @PathVariable String message) {
        ChatRequest chatRequest=new ChatRequest(userid,message);
        ChatResponse resp=lawyerServices.chatLawyer(chatRequest);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @DeleteMapping("/delete/dataset/{userid}")
    public ResponseEntity<APIResponse> deleteDataset(@PathVariable Long userid) throws IOException {
        lawyerServices.deleteDataset(userid);
        APIResponse apiResponse=new APIResponse("Deleted Successfully",true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}