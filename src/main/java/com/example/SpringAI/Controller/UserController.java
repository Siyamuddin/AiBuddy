package com.example.SpringAI.Controller;

import com.example.SpringAI.DTOs.UserDTO;
import com.example.SpringAI.Services.UserServices;
import com.example.SpringAI.Utility.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserServices userServices;
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO userDTOResponse=userServices.registerUser(userDTO);
        return new ResponseEntity<>(userDTOResponse, HttpStatus.CREATED);
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO,@PathVariable Long userId){
        UserDTO userDTOResponse=userServices.updateUser(userId,userDTO);
        return new ResponseEntity<>(userDTOResponse,HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId){
        UserDTO userDTO=userServices.getUser(userId);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long userId){
        boolean result= userServices.deleteUser(userId);
        if(result){
            APIResponse apiResponse=new APIResponse("The user is deleted successfully",true);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }
        else {
            APIResponse apiResponse=new APIResponse("The user can not be deleted.",false);
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);

        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                     @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                     @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                     @RequestParam(value = "sortDirection", defaultValue = "asc",required = false) String sortDirection){
        List<UserDTO> userDTOS=userServices.getAllUser(pageNumber,pageSize,sortBy,sortDirection);
        return new ResponseEntity<>(userDTOS,HttpStatus.OK);
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<List<UserDTO>> searchUser(@PathVariable String username,
                                                    @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                    @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                    @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                    @RequestParam(value = "sortDirection",defaultValue = "asc",required = false) String sortDirection
                                                    )
    {

        List<UserDTO> userDTOS=userServices.searchByUserName(username,pageNumber,pageSize,sortBy,sortDirection);
        return new ResponseEntity<>(userDTOS,HttpStatus.OK);
    }
}
