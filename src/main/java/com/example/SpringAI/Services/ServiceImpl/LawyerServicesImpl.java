package com.example.SpringAI.Services.ServiceImpl;

import com.example.SpringAI.Exceptions.ResourceNotFoundException;
import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Services.AIServices.RAGImpl;
import com.example.SpringAI.Services.LawyerServices;
import com.example.SpringAI.Utility.APIResponse;
import com.example.SpringAI.Utility.ChatRequest;
import com.example.SpringAI.Utility.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LawyerServicesImpl implements LawyerServices {
    @Autowired
    private RAGImpl rag;
    @Autowired
    private LocalUserRepo localUserRepo;
    @Value("${project.file}")
    private String PATH;
    @Autowired
    private FileService fileService;
    @Override
    public APIResponse uploadDataSet(Long userId, MultipartFile file) throws IOException {

        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","ID",userId));

        String location=fileService.uploadImage(PATH,file);

        APIResponse apiResponse=new APIResponse("File Uploaded",true);

        return apiResponse;
    }

    @Override
    public void deleteDataset(Long userId) throws IOException {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","ID",userId));

            Files.deleteIfExists(Paths.get("file/DATASET.pdf"));
            Files.deleteIfExists(Paths.get("file/embedding.store"));


    }

    @Override
    public ChatResponse chatLawyer(ChatRequest chatRequest){
        LocalUser localUser=localUserRepo.findById(chatRequest.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User","ID", chatRequest.getUserId()));

        String response=rag.lawyerRag(localUser.getEmail(), chatRequest.getMessage());
        ChatResponse chatResponse= new ChatResponse(response,localUser.getEmail(), HttpStatus.OK);

        return chatResponse;
    }
}
