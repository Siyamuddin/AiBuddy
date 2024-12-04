package com.example.SpringAI.Services.ServiceImpl;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.Exceptions.ResourceNotFoundException;
import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Model.Slide;
import com.example.SpringAI.Model.UserClass;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Repository.SlideRepo;
import com.example.SpringAI.Repository.UserClassRepo;
import com.example.SpringAI.Services.AIServices.RAGImpl;
import com.example.SpringAI.Services.SlideServices;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
@Slf4j
public class SlideServiceImpl implements SlideServices {
    @Autowired
    private UserClassRepo userClassRepo;
    @Autowired
    private LocalUserRepo localUserRepo;
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
    public Long uploadSlide(Long classId, MultipartFile file) throws IOException {
        //checking if the userClass is available or not
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()-> new ResourceNotFoundException("Class","class ID:",classId));
        log.info("Email :"+userClass.getLocalUser().getEmail());
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
        Slide resp=slideRepo.save(slide);
        //Mail confirmation that slide is ready for operation.
        mailSenderServices.sendEmail(userClass.getLocalUser().getEmail(),"AiBuddy mail Confirmation.","Your uploaded slide is ready to perform operations.");
        return resp.getId();
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
    public List<SlideDTO> getAllSlides(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort;
        if(sortDirection.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else {
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Slide> pages=slideRepo.findAll(pageable);
        List<SlideDTO> slideDTOS=pages.stream().map(page-> modelMapper.map(page,SlideDTO.class)).collect(Collectors.toUnmodifiableList());
        return slideDTOS;
    }











                                                    //Project


    @Override
    public SlideDTO userSlideUpload(Long userId, MultipartFile file) {
        //checking if the userClass is available or not
        LocalUser localUser;
        localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","ID",userId));
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
        slide.setLocalUser(localUser);
        //saving the slide.
        Slide resp=slideRepo.save(slide);
        //Mail confirmation that slide is ready for operation.
        mailSenderServices.sendEmail(localUser.getEmail(),"AiBuddy mail Confirmation.","Your uploaded slide is ready to perform operations.");
        return modelMapper.map(resp,SlideDTO.class);
    }


    @Override
    public List<SlideDTO> getAllUserSlide(Long userId) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","ID",userId));
        List<Slide> slides=slideRepo.findAllByLocalUserId(userId);
        List<SlideDTO> slideDTOS=slides.stream().map((slide)-> modelMapper.map(slide,SlideDTO.class)).collect(Collectors.toUnmodifiableList());
        return slideDTOS;
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
    public void deleteSlide(Long slideId) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        slideRepo.deleteById(slideId);
    }



    @Override
    public String generateShortQuestions(Long slideId, String numberOfQuestions) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        String questions = rag.generateRAGResponse2(slide.getSlideContent()," You are an AI assistant who helps student prepare for exam using their lecture slide, please Generate "+ numberOfQuestions+ " short questions and answers from this lecture that may asked int exam");
        slide.setGeneratedQuestions(questions);
        slideRepo.save(slide);

        String recipientEmail = slide.getLocalUser().getEmail();
        String slideTitle = slide.getSlideTitle();
        String subject = "AI Buddy - Mail Confirmation";
        String message = String.format(
                "Hello %s,\n\nYour AI-generated short Questions are ready! 🎉\n\nFrom Slide: %s\n\nThank you for using AI Buddy to enhance your productivity.\n\nBest regards,\nThe AI Buddy Team",
                slide.getLocalUser().getFirstName(), slideTitle);
        mailSenderServices.sendEmail(recipientEmail, subject, message);        return questions;
    }

    @Override
    public String generateMCQ(Long slideId, String numberOfMCQs) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        String MCQ=rag.generateRAGResponse2(slide.getSlideContent()," You are an AI assistant who helps student prepare for exam using their lecture slide, please Generate "+numberOfMCQs+" important multiple-choice questions (MCQs) based on the content. Ensure that each question has 4 options, and highlight the correct answer.");
        slide.setGeneratedMCQ(MCQ);
        slideRepo.save(slide);

        String recipientEmail = slide.getLocalUser().getEmail();
        String slideTitle = slide.getSlideTitle();
        String subject = "AI Buddy - Mail Confirmation";
        String message = String.format(
                "Hello %s,\n\nYour AI-generated MCQs are ready! 🎉\n\nFrom Slide: %s\n\nThank you for using AI Buddy to enhance your productivity.\n\nBest regards,\nThe AI Buddy Team",
                slide.getLocalUser().getFirstName(), slideTitle);
        mailSenderServices.sendEmail(recipientEmail, subject, message);
        return MCQ;
    }

    @Override
    public String generateSummary(Long slideId) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        //getting the AI generated summary of our given text.
        String summary = rag.generateRAGResponse2(slide.getSlideContent(),"Summarize the key points from the content above for effective revision, don't miss any important point.");
        slide.setSlideSummary(summary);
        slideRepo.save(slide);

        String recipientEmail = slide.getLocalUser().getEmail();
        String slideTitle = slide.getSlideTitle();
        String subject = "AI Buddy - Mail Confirmation";
        String message = String.format(
                "Hello %s,\n\nYour AI-generated short summary is ready! 🎉\n\nSlide Title: %s\n\nThank you for using AI Buddy to enhance your productivity.\n\nBest regards,\nThe AI Buddy Team",
                slide.getLocalUser().getFirstName(), slideTitle
        );

        mailSenderServices.sendEmail(recipientEmail, subject, message);

        return summary;
    }

    @Override
    public String writeAiQuery(Long slideId, String query) {
        Slide slide=slideRepo.findById(slideId).orElseThrow(()-> new ResourceNotFoundException("Slide","slide ID: ",slideId));
        String AiResponse=rag.generateRAGResponse2(slide.getSlideContent(),"  You are an AI assistant who helps student prepare for exam using their lecture slide, please answer the question "+query);
        return AiResponse;
    }


}
