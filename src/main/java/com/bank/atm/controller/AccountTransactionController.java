package com.bank.atm.controller;

import com.bank.atm.dto.AccountTransactionHistoryDTO;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.service.AccountService;
import com.bank.atm.service.AccountTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/transaction/")
@RestController
@CrossOrigin
public class AccountTransactionController {

    private final AccountTransactionService accountTransactionService;
    private final AccountService accountService;

    public AccountTransactionController(AccountTransactionService accountTransactionService, AccountService accountService) {
        this.accountTransactionService = accountTransactionService;
        this.accountService = accountService;
    }

    @GetMapping("history")
    public ResponseEntity<AccountTransactionHistoryDTO> getHistory() throws ATMDataValidationException {
        String accountNumber = accountService.getAuthenticatedAccountNumber();
        if (accountNumber == null) {
            throw new AuthenticationServiceException("Current token has expired");
        }
        return new ResponseEntity<>(accountTransactionService.getHistoryByAccountNumber(accountNumber), HttpStatus.OK);
    }
}
