package com.lalbrecht.mediasite.utils.custom_exceptions;

public class ResourceConflictException extends RuntimeException{
    public ResourceConflictException() {
    }

    public ResourceConflictException(String message) {
        super(message);
    }
}
