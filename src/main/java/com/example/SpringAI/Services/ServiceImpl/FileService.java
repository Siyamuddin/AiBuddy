package com.example.SpringAI.Services.ServiceImpl;

import com.example.SpringAI.Services.AIServices.RAGImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {
    @Autowired
    private RAGImpl rag;
    public String uploadImage(String path, MultipartFile file) throws IOException {

        //File name
        String name= file.getOriginalFilename();
        //generate a random name
        String randomID= "DATASET";
        String fileName1=randomID.concat(name.substring(name.lastIndexOf(".")));
        //Full path
        String filePath=path+ File.separator+fileName1;
        //create folder if not created
        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        rag.cleanAndStoreData(file.getInputStream());
        //copy file
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName1;
    }


    public InputStream getSource(String path, String fileName)  {
        String fullPath=path+File.separator+fileName;
        InputStream is= null;
        try {
            is = new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return is;
    }
}
