package com.masonord.harmonyhound.exception;

public class UnsupportedMediaType extends Exception {
    public UnsupportedMediaType(String message) {
        super(message);
    }
    public UnsupportedMediaType(Throwable e, String message) {
        super(message, e);
    }
}
