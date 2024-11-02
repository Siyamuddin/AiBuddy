package com.example.SpringAI.DTOs;

import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Model.Slide;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class UserClassDTO {
    private Long id;
    private String className;
    private String professorName;
    private String description;
    private List<SlideDTO> slideList=new ArrayList<>();
    private UserDTO user;

}
