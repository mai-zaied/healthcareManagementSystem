package com.example.healthcareappointmentsystem.exception;

public class ResourceNotFoundException extends BusinessException{
    public ResourceNotFoundException(String resourceName, Long id){
        super(resourceName + " not found with id: " + id);
    }
    public ResourceNotFoundException(String msg, String identifier) {
        super(msg +  identifier);
    }
}
