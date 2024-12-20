package com.example.SpringAI.Services;

import com.example.SpringAI.DTOs.SlideDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SlideServices {

    SlideDTO userSlideUpload(Long userId,MultipartFile file);
    SlideDTO getSlide(Long slideId);
    List<SlideDTO> getAllUserSlide(Long userId);
    void deleteSlide(Long slideId);
    String generateShortQuestions(Long slideId,String numberOfQuestions);
    String generateMCQ(Long slideId,String numberOfMCQs);
    String generateSummary(Long slideId);
    String writeAiQuery(Long slideId, String query);

    Long uploadSlide(Long classId, MultipartFile file) throws IOException;
    SlideDTO updateSlide(Long slideId, SlideDTO slideDTO);
    List getAllSlidesByClass(Long classId,int pageNumber,int pageSize,String sortBy,String sortDirection);
    List<SlideDTO> getAllSlides(int pageNumber, int pageSize, String sortBy, String sortDirection);


}
