package com.example.demo.exceptions;

public class DelayInterruptedException extends RuntimeException {
    public DelayInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
