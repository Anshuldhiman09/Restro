package com.restro.main.exceptions;

public class DuplicateEntryExc extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateEntryExc(String message) {
        super(message);
    }

    public DuplicateEntryExc(String message, Throwable cause) {
        super(message, cause);
    }
}
