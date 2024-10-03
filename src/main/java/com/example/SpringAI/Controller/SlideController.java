package com.example.SpringAI.Controller;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.Services.SlideServices;
import com.example.SpringAI.Utility.APIResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.POST;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/slide")
public class SlideController {
    @Autowired
    private SlideServices slideServices;
    @PostMapping("/upload/{classId}")
    public ResponseEntity<APIResponse> uploadSlide(@PathVariable Long classId,
                                                   @RequestParam(value = "file",required = true)MultipartFile file) throws IOException {
        slideServices.uploadSlide(classId,file);
        APIResponse apiResponse=new APIResponse("Slide is uploaded sucessfully",true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/get/{slideId}")
    public ResponseEntity<SlideDTO> getSlide(@PathVariable Long slideId)
    {
        SlideDTO slideDTO=slideServices.getSlide(slideId);
        return new ResponseEntity<>(slideDTO,HttpStatus.OK);
    }
}
