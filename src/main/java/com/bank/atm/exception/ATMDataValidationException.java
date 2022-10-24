package com.bank.atm.exception;

import org.springframework.http.HttpStatus;

public class ATMDataValidationException extends ATMException {

    private static final HttpStatus statusCode = HttpStatus.NOT_FOUND;

    public ATMDataValidationException(String errorMessage) {
        super(errorMessage, statusCode);
    }
}
