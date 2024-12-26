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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

// List to store text from each page
        List<String> pagesText = new ArrayList<>();

        try {
            // Check if the document has pages
            PDDocument document=PDDocument.load(file.getInputStream());
            if (!document.isEncrypted()) {
                PDFTextStripper stripper = new PDFTextStripper();
                int totalPages = document.getNumberOfPages();

                for (int i = 0; i < totalPages; i++) {
                    stripper.setStartPage(i + 1);
                    stripper.setEndPage(i + 1);
                    String pageText = stripper.getText(document);
                    pagesText.add(pageText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = pagesText.stream()
                .map(page -> page.replaceAll("\n", " ") + "\n\n\n")
                .collect(Collectors.joining());

        log.info("Content :"+content);
        System.out.println(content);
        //Creating a slide object.
        Slide slide=new Slide();
        slide.setSlideContent(content);
        //Setting up the slide fields.
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
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","ID",userId));
        List<String> pagesText = new ArrayList<>();

        try {
            // Check if the document has pages
            PDDocument document=PDDocument.load(file.getInputStream());
            if (!document.isEncrypted()) {
                PDFTextStripper stripper = new PDFTextStripper();
                int totalPages = document.getNumberOfPages();

                for (int i = 0; i < totalPages; i++) {
                    stripper.setStartPage(i + 1);
                    stripper.setEndPage(i + 1);
                    String pageText = stripper.getText(document);
                    pagesText.add(pageText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = pagesText.stream()
                .map(page -> page.replaceAll("\n", " ") + "\n\n\n")
                .collect(Collectors.joining());

        //Creating a slide object.
        Slide slide=new Slide();

        //Setting up the slide fields.
        slide.setSlideContent(content);
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
        String questions = rag.generateRAGResponse(slide.getSlideContent(),"Generate "+ numberOfQuestions+ " possible short questions and answers from this lecture");
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
        String MCQ=rag.generateRAGResponse2(slide.getSlideContent(),"Generate "+numberOfMCQs+" multiple-choice questions (MCQs) based on the content. Ensure that each question has 4 options, and highlight the correct answer.");
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
        String prompt="""
                The provided content contains summaries of individual pages from a lecture slide.
                 Your task is to synthesize these summaries into a cohesive and concise overall summary of the lecture.
                  The goal is to make the content easier to review and prepare for the exam.
                """;

        String summary = rag.generateRAGResponse2(slide.getSlideContent(),prompt);
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
        String AiResponse=rag.generateRAGResponse(slide.getSlideContent(),query);
        return AiResponse;
    }


}