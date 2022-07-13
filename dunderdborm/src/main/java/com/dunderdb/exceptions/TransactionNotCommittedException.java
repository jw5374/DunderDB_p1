package com.dunderdb.exceptions;

public class TransactionNotCommittedException extends RuntimeException {
    public TransactionNotCommittedException() {
        super("You have not committed a previously begun transaction yet");
    }
}
