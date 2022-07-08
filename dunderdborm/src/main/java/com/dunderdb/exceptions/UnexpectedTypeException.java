package com.dunderdb.exceptions;

public class UnexpectedTypeException extends RuntimeException {
    public UnexpectedTypeException() {
        super("That is an unexpected type. Cannot convert to SQL Type.");
    }
}
