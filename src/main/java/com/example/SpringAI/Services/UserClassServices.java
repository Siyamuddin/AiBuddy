package com.example.SpringAI.Services;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.DTOs.UserClassDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserClassServices {
    UserClassDTO createNewClass(UserClassDTO userClassDTO, Long userId);
    UserClassDTO updateClass(UserClassDTO userClassDTO, Long classId);
    UserClassDTO getClass(Long classId);
    List<UserClassDTO> getAllClass(int pageNumber, int pageSize, String sortBy, String sortDirection);
    void deleteClass(Long classId);
    List<SlideDTO> getAllSlideByClass(Long classId);
}
