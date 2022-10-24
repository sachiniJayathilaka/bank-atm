package com.bank.atm.exception;

import org.springframework.http.HttpStatus;

public class ATMOperationException extends ATMException {
    private static final HttpStatus statusCode = HttpStatus.NOT_FOUND;

    public ATMOperationException(String errorMessage) {
        super(errorMessage, statusCode);
    }
}
