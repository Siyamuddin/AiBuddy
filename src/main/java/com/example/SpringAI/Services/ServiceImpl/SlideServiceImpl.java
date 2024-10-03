package com.example.SpringAI.Services.ServiceImpl;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.Exceptions.ResourceNotFoundException;
import com.example.SpringAI.Model.Slide;
import com.example.SpringAI.Model.UserClass;
import com.example.SpringAI.Repository.SlideRepo;
import com.example.SpringAI.Repository.UserClassRepo;
import com.example.SpringAI.Services.AIServices.RAGImpl;
import com.example.SpringAI.Services.SlideServices;
import dev.langchain4j.data.document.Document;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
@Slf4j
public class SlideServiceImpl implements SlideServices {
    @Autowired
    private UserClassRepo userClassRepo;
    @Autowired
    private SlideRepo slideRepo;
    @Autowired
    private RAGImpl rag;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    @Transactional
    public void uploadSlide(Long classId, MultipartFile file) throws IOException {
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()-> new ResourceNotFoundException("Class","class ID:",classId));

        String text= null;
        String MCQ= null;
        String questions= null;
        String summary= null;
        try {
            PDDocument document=PDDocument.load(file.getInputStream());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
            MCQ = rag.generateRAGResponse(text,"Generate 15 possible Multiple choice questions from this lecture");
            questions = rag.generateRAGResponse(text,"Generate 10 possible short questions and answers from this lecture");
            summary = rag.generateRAGResponse(text,"Generate summary from this lecture");
        } catch (IOException e) {
            log.error("Error: "+e);
        }

        Slide slide=new Slide();

        slide.setSlideContent(text);
        slide.setSlideSummary(summary);
        slide.setSlideTitle(file.getOriginalFilename());
        slide.setGeneratedMCQ(MCQ);
        slide.setGeneratedQuestions(questions);
        slide.setUserclass(userClass);

        slideRepo.save(slide);

    }

    @Override
    public SlideDTO updateSlide(Long slideId, SlideDTO slideDTO) {
        return null;
    }

    @Override
    public SlideDTO getSlide(Long slideId) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));

        return modelMapper.map(slide,SlideDTO.class);
    }

    @Override
    public void delete(Long slideId) {

    }
}
