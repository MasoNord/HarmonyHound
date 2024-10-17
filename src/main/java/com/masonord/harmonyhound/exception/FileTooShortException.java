package com.masonord.harmonyhound.exception;

public class FileTooShortException extends Exception{
    public FileTooShortException(String message) {
        super(message);
    }

    public FileTooShortException(Throwable e, String message) {
        super(message, e);
    }
}
