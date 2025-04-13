package com.example.demo.exceptions;

public class UnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super("Unauthorized access");
    }
    
    public UnauthorizedException(String message) {
        super(message);
    }
}