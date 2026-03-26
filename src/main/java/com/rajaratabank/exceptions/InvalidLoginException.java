package com.rajaratabank.exceptions;

/**
 * Custom exception representing a failed login attempt.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
