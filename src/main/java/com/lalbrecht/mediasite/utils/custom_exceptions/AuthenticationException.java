package com.lalbrecht.mediasite.utils.custom_exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

}
