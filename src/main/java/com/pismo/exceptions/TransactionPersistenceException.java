package com.pismo.exceptions;

public class TransactionPersistenceException extends RuntimeException {
    public TransactionPersistenceException(String s) {
        super(s);
    }
}
