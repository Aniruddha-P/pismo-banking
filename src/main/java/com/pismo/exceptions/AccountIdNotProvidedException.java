package com.pismo.exceptions;

public class AccountIdNotProvidedException extends RuntimeException {
    public AccountIdNotProvidedException(String s) {
        super(s);
    }
}
