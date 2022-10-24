package com.bank.atm.exception;

import com.bank.atm.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ATMException.class)
    public ResponseEntity<ErrorResponseDTO> handleATMException(ATMException ex) {
        log.error(ex.getErrorMessage(), ex);
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getErrorMessage()), ex.getStatusCode());
    }
}
