package com.bp.service.exception;

public class InactiveAccountException extends RuntimeException{
    public InactiveAccountException(String message) {
        super(message);
    }
}
