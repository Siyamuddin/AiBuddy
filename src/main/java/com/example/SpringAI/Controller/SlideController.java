package com.example.SpringAI.Controller;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.Repository.SlideRepo;
import com.example.SpringAI.Services.SlideServices;
import com.example.SpringAI.Utility.APIResponse;
import dev.langchain4j.agent.tool.P;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/slide")
public class SlideController {
    @Autowired
    private SlideServices slideServices;

    @PostMapping(value = "/upload/{classId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadSlide(@PathVariable Long classId,
                                                   @RequestParam(value = "file", required = true) MultipartFile file) throws IOException {

        
        Long resp=slideServices.uploadSlide(classId, file);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/get/{slideId}")
    public ResponseEntity<SlideDTO> getSlide(@PathVariable Long slideId) {
        SlideDTO slideDTO = slideServices.getSlide(slideId);
        return new ResponseEntity<>(slideDTO, HttpStatus.OK);
    }

    @GetMapping("/get/all/slide/{classId}")
    public ResponseEntity<List<SlideDTO>> getAllSlideByClass(@PathVariable Long classId,
                                                             @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                             @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection
    ) {
        List<SlideDTO> response = slideServices.getAllSlidesByClass(classId, pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/generate/sq/{slideId}/{nsq}")
    public ResponseEntity<String> generateShortQuestions(@PathVariable Long slideId, @PathVariable String nsq) {
        if (nsq.startsWith("3")) {
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

    @GetMapping("/get/all")
    public ResponseEntity<List<SlideDTO>> getAllSlide(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                      @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                      @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        List<SlideDTO> response=slideServices.getAllSlides(pageNumber,pageSize,sortBy,sortDirection);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
