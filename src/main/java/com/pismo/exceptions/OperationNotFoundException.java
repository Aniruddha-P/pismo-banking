package com.pismo.exceptions;

public class OperationNotFoundException extends RuntimeException {
    public OperationNotFoundException(String s) {
        super(s);
    }
}
