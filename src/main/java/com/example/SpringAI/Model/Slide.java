package com.example.SpringAI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Getter
@Setter
@Entity
@Table(name = "slide")
public class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String slideTitle;
    private String slideContent;
    private String slideSummary;
    private String generatedQuestions;
    @ManyToOne
    @JoinColumn(name = "userclass_id")
    private UserClass userclass;

}