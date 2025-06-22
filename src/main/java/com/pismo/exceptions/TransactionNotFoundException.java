package com.pismo.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String s) {
        super(s);
    }
}
