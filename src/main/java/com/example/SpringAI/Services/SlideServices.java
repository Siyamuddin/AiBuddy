package com.example.SpringAI.Services;

import com.example.SpringAI.DTOs.SlideDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SlideServices {
    void uploadSlide(Long classId, MultipartFile file) throws IOException;
    SlideDTO updateSlide(Long slideId, SlideDTO slideDTO);
    SlideDTO getSlide(Long slideId);
    void delete(Long slideId);


}
