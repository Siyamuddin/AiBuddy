package com.example.SpringAI.Services.ServiceImpl;

import com.example.SpringAI.DTOs.UserDTO;
import com.example.SpringAI.Exceptions.ResourceAlreadyExist;
import com.example.SpringAI.Exceptions.ResourceNotFoundException;
import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserServices {
    @Autowired
    private LocalUserRepo localUserRepo;
    @Autowired
   private ModelMapper modelMapper;

    @Override
    public UserDTO registerUser(UserDTO userDTO){

        List<LocalUser> userList=localUserRepo.findByEmailContainingIgnoreCase(userDTO.getEmail());
        if(userList.isEmpty()){
            LocalUser localUser=modelMapper.map(userDTO, LocalUser.class);
            LocalUser savedUser=localUserRepo.save(localUser);
            return modelMapper.map(savedUser,UserDTO.class);
        }
        else {
            log.error("User already exist exception has been thrown, User Registration request:"+ userDTO);
            throw new ResourceAlreadyExist(userDTO.getFirstName()+" "+userDTO.getLastName(),userList.get(0).getId());

        }

    }
    @Override
    public List<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String direction) {
        Sort sort;
        if(direction.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else{
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<LocalUser> localUsers=localUserRepo.findAll(pageable);
        List<UserDTO> userDTOS=localUsers.stream().map((localUser -> modelMapper.map(localUser,UserDTO.class))).collect(Collectors.toUnmodifiableList());

        return userDTOS;
    }
    @Override
    public List<UserDTO> searchByUserName(String userName, int pageNumber, int pageSize, String sortBy, String direction) {
        Sort sort;
        if(direction.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else {
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<LocalUser> localUsers=localUserRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(userName,userName,pageable);
        List<UserDTO> userDTOS=localUsers.stream().map((user)-> modelMapper.map(user,UserDTO.class)).collect(Collectors.toUnmodifiableList());

        return userDTOS;
    }






                                                     //Project


    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));
        localUser.setFirstName(userDTO.getFirstName());
        localUser.setLastName(userDTO.getLastName());
        localUser.setEmail(userDTO.getEmail());
//        localUser.setPassword(userDTO.getPassword());
        localUser.setUniversity(userDTO.getUniversity());
        localUser.setDepartment(userDTO.getDepartment());
        localUser.setCountry(userDTO.getCountry());
        LocalUser savedLocalUser=localUserRepo.save(localUser);
        return modelMapper.map(savedLocalUser,UserDTO.class);
    }

    @Override
    public UserDTO getUser(Long userId) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));

        return modelMapper.map(localUser,UserDTO.class);
    }

    @Override
    public boolean deleteUser(Long userId) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));
        try {
            localUserRepo.deleteById(userId);
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
return true;
    }






}
