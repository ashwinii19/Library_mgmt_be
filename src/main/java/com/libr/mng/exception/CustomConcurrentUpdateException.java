package com.libr.mng.exception;

public class CustomConcurrentUpdateException extends RuntimeException {
    public CustomConcurrentUpdateException(String message) {
        super(message);
    }
}