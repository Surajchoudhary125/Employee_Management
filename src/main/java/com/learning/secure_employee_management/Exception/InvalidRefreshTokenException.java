package com.learning.secure_employee_management.Exception;

    public class InvalidRefreshTokenException extends RuntimeException {
     public InvalidRefreshTokenException(String message) {
        super(message);
    }
}