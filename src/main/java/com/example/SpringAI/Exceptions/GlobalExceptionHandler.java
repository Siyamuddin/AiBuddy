package com.example.SpringAI.Exceptions;

import com.example.SpringAI.Utility.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundException(ResourceNotFoundException exception){
        APIResponse apiResponse=new APIResponse(exception.getMessage(),false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourceAlreadyExist.class)
    public ResponseEntity<APIResponse> resourceAlreadyExistException(ResourceAlreadyExist resourceAlreadyExist){
        APIResponse apiResponse=new APIResponse(resourceAlreadyExist.getMessage(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);

    }
}
