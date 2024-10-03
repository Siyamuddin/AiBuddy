package com.example.SpringAI.Services.ServiceImpl;

import com.example.SpringAI.DTOs.SlideDTO;
import com.example.SpringAI.DTOs.UserClassDTO;
import com.example.SpringAI.Exceptions.ResourceNotFoundException;
import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Model.Slide;
import com.example.SpringAI.Model.UserClass;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Repository.SlideRepo;
import com.example.SpringAI.Repository.UserClassRepo;
import com.example.SpringAI.Services.AIServices.ConfigLangChain;
import com.example.SpringAI.Services.UserClassServices;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserClassServiceImpl implements UserClassServices {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserClassRepo userClassRepo;
    @Autowired
    private SlideRepo slideRepo;
    @Autowired
    private LocalUserRepo localUserRepo;
    @Autowired
    private ConfigLangChain model;

    @Override
    public UserClassDTO createNewClass(UserClassDTO userClassDTO, Long userId) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID:",userId));

        UserClass userClass=modelMapper.map(userClassDTO,UserClass.class);
        userClass.setLocalUser(localUser);
        UserClass response=userClassRepo.save(userClass);

        return modelMapper.map(response,UserClassDTO.class);
    }

    @Override
    public UserClassDTO updateClass(UserClassDTO userClassDTO, Long classId) {
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()->new ResourceNotFoundException("Class","class ID:",classId));

        userClass.setClassName(userClassDTO.getClassName());
        userClass.setDescription(userClassDTO.getDescription());
        userClass.setProfessorName(userClassDTO.getProfessorName());
        UserClass updatedUserClass=userClassRepo.save(userClass);



        return modelMapper.map(updatedUserClass,UserClassDTO.class);
    }

    @Override
    public UserClassDTO getClass(Long classId) {
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()->new ResourceNotFoundException("Class","class ID",classId));
        return modelMapper.map(userClass,UserClassDTO.class);
    }

    @Override
    public List<UserClassDTO> getAllClass(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort;
        if(sortDirection.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else {
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<UserClass> userClasses=userClassRepo.findAll(pageable);
        List<UserClassDTO> userClassDTOList=userClasses.stream().map((userClass)-> modelMapper.map(userClass,UserClassDTO.class)).collect(Collectors.toUnmodifiableList());

        return userClassDTOList;
    }

    @Override
    public void deleteClass(Long classId) {
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()->new ResourceNotFoundException("Class","class ID",classId));
        userClassRepo.delete(userClass);
    }

    @Override
    public List<SlideDTO> getAllSlideByClass(Long classId) {
        UserClass userClass=userClassRepo.findById(classId).orElseThrow(()->new ResourceNotFoundException("Class","class ID",classId));
        List<Slide> slides=slideRepo.findAllByUserclass(userClass);
        List<SlideDTO> slideDTOS=slides.stream().map((slide)-> modelMapper.map(slide,SlideDTO.class)).collect(Collectors.toUnmodifiableList());

        return slideDTOS;
    }

}
