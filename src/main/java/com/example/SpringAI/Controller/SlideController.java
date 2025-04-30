package com.example.SpringAI.Controller;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.Repository.SlideRepo;
import com.example.SpringAI.Services.SlideServices;
import com.example.SpringAI.Utility.APIResponse;
import dev.langchain4j.agent.tool.P;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.ManyToOne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/slide")
public class SlideController {
    @Autowired
    private SlideServices slideServices;

                                                 //Project


    @GetMapping("/get/{slideId}")
    public ResponseEntity<SlideDTO> getSlide(@PathVariable Long slideId) {
        SlideDTO slideDTO = slideServices.getSlide(slideId);
        return new ResponseEntity<>(slideDTO, HttpStatus.OK);
    }

    @GetMapping("/generate/sq/{slideId}/{nsq}")
    public ResponseEntity<String> generateShortQuestions(@PathVariable Long slideId, @PathVariable String nsq) {
        if (nsq.startsWith("3")) {
            log.error("Asking for more than 20 questions");
            throw new RuntimeException("You can not ask for more than 25 questions.");
        } else {
            String response = slideServices.generateShortQuestions(slideId, nsq);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @GetMapping("/generate/mcq/{slideId}/{nq}")
    public ResponseEntity<String> generateMCQ(@PathVariable Long slideId, @PathVariable String nq) {
        if (nq.startsWith("3")) {
            throw new RuntimeException("You can not ask for more than 25 questions.");
        } else {
            String response = slideServices.generateMCQ(slideId,nq);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
    @GetMapping("/generate/summary/{slideId}")
    public ResponseEntity<String> generateSummary(@PathVariable Long slideId)
    {
        String response=slideServices.generateSummary(slideId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/query/{slideId}/{query}")
    public ResponseEntity<String> queryOnSlide(@PathVariable Long slideId, @PathVariable String query)
    {
        String response=slideServices.writeAiQuery(slideId,query);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{slideId}")
    public ResponseEntity<APIResponse> deleteSlide(@PathVariable Long slideId)
    {
        slideServices.deleteSlide(slideId);
        APIResponse apiResponse=new APIResponse("Slide is deleted Successfully",true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }


    @PostMapping(value = "/upload/user/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SlideDTO> uploadSlideByUser(@PathVariable Long userId,
                                                      @RequestParam(value = "file",required = true) MultipartFile file){
        SlideDTO slideDTO=slideServices.userSlideUpload(userId,file);
        return new ResponseEntity<>(slideDTO,HttpStatus.OK);
    }

    @GetMapping("/get/all/user/{userId}")
    public ResponseEntity<List<SlideDTO>> getAllSlideByUser(@PathVariable Long userId){
        List<SlideDTO> slideDTOS=slideServices.getAllUserSlide(userId);
        return new ResponseEntity<>(slideDTOS,HttpStatus.OK);
    }
}
