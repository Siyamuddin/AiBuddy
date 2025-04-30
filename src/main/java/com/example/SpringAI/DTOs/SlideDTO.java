package com.example.SpringAI.DTOs;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SlideDTO {
    private Long id;
    private String slideTitle;
    private String slideContent;
    private String slideSummary;
    private String generatedMCQ;
    private String generatedQuestions;
}
