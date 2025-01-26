package com.example.SpringAI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(columnDefinition = "MEDIUMTEXT")
    private String slideContent;
    @Column(columnDefinition = "TEXT")
    private String slideSummary;
    @Column(columnDefinition = "TEXT")
    private String generatedMCQ;
    @Column(columnDefinition = "TEXT")
    private String generatedQuestions;
    @ManyToOne
    @JoinColumn(name = "userclass")
    private UserClass userclass;

    @ManyToOne
    @JoinColumn(name = "localUser")
    private LocalUser localUser;

}