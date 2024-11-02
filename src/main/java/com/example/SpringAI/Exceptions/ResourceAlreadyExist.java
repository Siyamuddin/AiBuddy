package com.example.SpringAI.Exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceAlreadyExist extends RuntimeException {
  private String username;
  private Long id;
    public ResourceAlreadyExist(String username,Long id) {
        super(String.format("User "+username+" with user ID:"+id+" is alreeady exist with this email."));
        this.username=username;
        this.id=id;
    }
}
