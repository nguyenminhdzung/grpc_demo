package com.dmn.assignment.task1.exception;

public class CinemaException extends RuntimeException {

    private static final long serialVersionUID = -2807564902123350645L;

    public CinemaException(String message){
        super(message);
    }

    public CinemaException(Exception innerException){
        super(innerException);
    }

    public CinemaException(String message, Exception innerException){
        super(message, innerException);
    }
}