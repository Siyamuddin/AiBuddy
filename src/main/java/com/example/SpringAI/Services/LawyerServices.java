package com.example.SpringAI.Services;



import com.example.SpringAI.Utility.APIResponse;
import com.example.SpringAI.Utility.ChatRequest;
import com.example.SpringAI.Utility.ChatResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LawyerServices {
    APIResponse uploadDataSet(Long userId, MultipartFile file) throws IOException;
    void deleteDataset(Long userId) throws IOException;
    ChatResponse chatLawyer(ChatRequest chatRequest);
}