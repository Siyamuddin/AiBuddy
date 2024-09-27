package com.example.SpringAI.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private Long fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue) {
        super(String.format(resourceName+"is not found with"+fieldName+": "+fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
