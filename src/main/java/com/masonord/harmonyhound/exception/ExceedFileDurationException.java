package com.masonord.harmonyhound.exception;

public class ExceedFileDurationException extends Exception {
    public ExceedFileDurationException(String message) {
        super(message);
    }

    public ExceedFileDurationException(Throwable e, String message) {
        super(message, e);
    }
}
