package com.vidyasagar.attendance.exception;

public class ResourceNotFoundException extends RuntimeException {
    // constructor to accept custom error message
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
