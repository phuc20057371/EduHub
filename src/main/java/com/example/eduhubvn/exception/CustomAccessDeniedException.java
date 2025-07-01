package com.example.eduhubvn.exception;

public class CustomAccessDeniedException extends RuntimeException{
    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
