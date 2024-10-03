package com.example.SpringAI.Controller;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.DTOs.UserClassDTO;
import com.example.SpringAI.Services.AIServices.ConfigLangChain;
import com.example.SpringAI.Services.UserClassServices;
import com.example.SpringAI.Utility.APIResponse;
import com.google.gson.annotations.JsonAdapter;
import dev.langchain4j.model.chat.request.ChatRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userclass")
public class UserClassController {
    @Autowired
    private UserClassServices userClassServices;
    @Autowired
    private ConfigLangChain ollama;
    @PostMapping("/create/{userid}")
    public ResponseEntity<UserClassDTO> createUserClass(@RequestBody UserClassDTO userClassDTO,@PathVariable Long userid)
    {
        UserClassDTO response=userClassServices.createNewClass(userClassDTO,userid);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{classId}")
    public ResponseEntity<UserClassDTO> updateUserClass(@RequestBody UserClassDTO userClassDTO,@PathVariable Long classId)
    {
        UserClassDTO response=userClassServices.updateClass(userClassDTO,classId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/get/{classId}")
    public ResponseEntity<UserClassDTO> getUserClass(@PathVariable Long classId)
    {
        UserClassDTO response=userClassServices.getClass(classId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserClassDTO>> getAllUserClass(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                              @RequestParam(value="pageSize",defaultValue = "5",required = false) int pageSize,
                                                              @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                              @RequestParam(value = "sortDirection",defaultValue = "asc",required = false) String sortDirection
                                                              )
    {
        List<UserClassDTO> response=userClassServices.getAllClass(pageNumber,pageSize,sortBy,sortDirection);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get/all/slide/{classId}")
    public ResponseEntity<List<SlideDTO>> getAllSlideByClass(@PathVariable Long classId){
        List<SlideDTO> response=userClassServices.getAllSlideByClass(classId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{classId}")
    public ResponseEntity<APIResponse> deleteClass(@PathVariable Long classId){
        userClassServices.deleteClass(classId);
        APIResponse apiResponse=new APIResponse("User class "+classId+" is deleted Successfully", true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/chat/{message}")
    public ResponseEntity<String> chat(@PathVariable String message){
        String response=ollama.chatClient().generate(message);
//        APIResponse apiResponse=new APIResponse(response,true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
