package com.masonord.harmonyhound.exception;

public class ExceedFileSizeLimitException extends Exception{

    public ExceedFileSizeLimitException(String message) {
        super(message);
    }

    public  ExceedFileSizeLimitException(Throwable e, String message) {
        super(message, e);
    }
}
