package com.example.SpringAI.Exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DatasetNotAvailable extends RuntimeException{
  private String message;
  public DatasetNotAvailable(String massage) {
    super(String.format(massage));
    this.message=massage;
  }
}