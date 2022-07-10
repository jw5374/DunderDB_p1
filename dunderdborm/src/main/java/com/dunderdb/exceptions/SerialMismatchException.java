package com.dunderdb.exceptions;

public class SerialMismatchException extends RuntimeException {
    public SerialMismatchException() {
        super("Serial type must be int.");
    }
}
