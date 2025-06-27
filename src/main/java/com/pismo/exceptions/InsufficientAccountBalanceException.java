package com.pismo.exceptions;

public class InsufficientAccountBalanceException extends RuntimeException {
    public InsufficientAccountBalanceException(String s) {
        super(s);
    }
}
