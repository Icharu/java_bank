package org.example.exception;

public class PixAlreadyInUseException extends RuntimeException {
    public PixAlreadyInUseException(String message) {
        super(message);
    }
    
}
