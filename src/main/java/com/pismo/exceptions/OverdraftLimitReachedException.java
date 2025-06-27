package com.pismo.exceptions;

public class OverdraftLimitReachedException extends RuntimeException {
    public OverdraftLimitReachedException(String s) {
        super(s);
    }
}
