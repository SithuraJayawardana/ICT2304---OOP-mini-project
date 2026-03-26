package com.rajaratabank.exceptions;

/**
 * Custom exception representing an unauthorized attempt to access a resource.
 */
public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
