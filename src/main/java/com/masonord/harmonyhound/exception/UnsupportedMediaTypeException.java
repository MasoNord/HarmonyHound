package com.masonord.harmonyhound.exception;

public class UnsupportedMediaTypeException extends Exception {
    public UnsupportedMediaTypeException(String message) {
        super(message);
    }
    public UnsupportedMediaTypeException(Throwable e, String message) {
        super(message, e);
    }
}