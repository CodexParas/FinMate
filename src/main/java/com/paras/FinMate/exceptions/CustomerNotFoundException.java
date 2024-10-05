package com.paras.FinMate.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException (String message) {
        super(message);
    }
}
