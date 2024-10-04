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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private MailSenderServices mailSenderServices;
    @Override
    @Transactional
    public void uploadSlide(Long classId, MultipartFile file) throws IOException {
        //checking if the userClass is available or not
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()-> new ResourceNotFoundException("Class","class ID:",classId));
        String text= null;
        try {
            //converts the PDF file in to String Text
            PDDocument document=PDDocument.load(file.getInputStream());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
        } catch (IOException e) {
            log.error("Error: "+e);
        }
        //Creating a slide object.
        Slide slide=new Slide();

        //Setting up the slide fields.
        slide.setSlideContent(text);
        slide.setSlideTitle(file.getOriginalFilename());
        //Setting slides userClass
        slide.setUserclass(userClass);
        //saving the slide.
        slideRepo.save(slide);
        //Mail confirmation that slide is ready for operation.
        mailSenderServices.sendEmail(userClass.getLocalUser().getEmail(),"AiBuddy mail Confirmation.","Your uploaded slide is ready to perform operations.");

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
    public List getAllSlidesByClass(Long classId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()->new ResourceNotFoundException("Class","class ID",classId));

        Sort sort;
        if(sortDirection.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else {
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Slide> slides=slideRepo.findAllByUserclass(userClass,pageable);
        List<SlideDTO> slideDTOS=slides.stream().map((slide)-> modelMapper.map(slide,SlideDTO.class)).collect(Collectors.toUnmodifiableList());
        return slideDTOS;
    }

    @Override
    public String generateShortQuestions(Long slideId, String numberOfQuestions) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        String questions = rag.generateRAGResponse(slide.getSlideContent(),"Generate "+ numberOfQuestions+ " possible short questions and answers from this lecture");
        slide.setGeneratedQuestions(questions);
        slideRepo.save(slide);
        mailSenderServices.sendEmail(slide.getUserclass().getLocalUser().getEmail(),"AiBuddy mail Confirmation.",numberOfQuestions+" Short-Questions are generated from :"+slide.getSlideTitle());

        return questions;
    }

    @Override
    public String generateMCQ(Long slideId, String numberOfMCQs) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        String MCQ=rag.generateRAGResponse(slide.getSlideContent(),"Generate "+numberOfMCQs+" possible Multiple choice questions from this lecture");
        slide.setGeneratedMCQ(MCQ);
        slideRepo.save(slide);
        mailSenderServices.sendEmail(slide.getUserclass().getLocalUser().getEmail(),"AiBuddy mail Confirmation.",numberOfMCQs+" MCQs are generated from :"+slide.getSlideTitle());

        return MCQ;
    }

    @Override
    public String generateSummary(Long slideId) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        //getting the AI generated summary of our given text.
        String summary = rag.generateRAGResponse(slide.getSlideContent(),"Generate summary of this lecture");
        slide.setSlideSummary(summary);
        slideRepo.save(slide);
        mailSenderServices.sendEmail(slide.getUserclass().getLocalUser().getEmail(),"AiBuddy mail Confirmation.","A short summary is generated from :"+slide.getSlideTitle());

        return summary;
    }

    @Override
    public String writeAiQuery(Long slideId, String query) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        String AiResponse=rag.generateRAGResponse(slide.getSlideContent(),query);

        return AiResponse;
    }

    @Override
    public void deleteSlide(Long slideId) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        slideRepo.deleteById(slideId);
    }
}
