package com.Hieu2k3.course.exception;

public class InvalidTokenException extends AppException {
    public InvalidTokenException() {
        super(ErrorCode.UNAUTHENTICATED);
    }
}