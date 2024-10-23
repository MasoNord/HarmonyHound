package com.masonord.harmonyhound.exception;

public class SongNotFoundException extends Exception {
    public SongNotFoundException(String message) {
        super(message);
    }

    public SongNotFoundException(Throwable e, String message) {
        super(message, e);
    }
}
