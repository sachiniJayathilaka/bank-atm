package com.bank.atm.controller;

import com.bank.atm.dto.AccountBalanceResponseDTO;
import com.bank.atm.dto.AccountRequestDTO;
import com.bank.atm.dto.AccountResponseDTO;
import com.bank.atm.dto.WithdrawalResponseDTO;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.exception.ATMOperationException;
import com.bank.atm.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping("/api/v1/accounts/")
@RestController
@CrossOrigin
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("create")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) throws ATMDataValidationException {
        return new ResponseEntity<>(accountService.createAccount(accountRequestDTO), HttpStatus.OK);
    }

    @GetMapping("balance")
    public ResponseEntity<AccountBalanceResponseDTO> getAccountBalance() throws ATMDataValidationException {
        String accountNumber = accountService.getAuthenticatedAccountNumber();
        if (accountNumber == null) {
            throw new AuthenticationServiceException("Current token has expired");
        }
        return new ResponseEntity<>(accountService.getAccountBalance(accountNumber), HttpStatus.OK);
    }

    @PutMapping("withdraw")
    public ResponseEntity<WithdrawalResponseDTO> withdraw(@RequestParam BigDecimal withdrawalAmount) throws ATMOperationException, ATMDataValidationException {
        String accountNumber = accountService.getAuthenticatedAccountNumber();
        if (accountNumber == null) {
            throw new AuthenticationServiceException("Current token has expired");
        }
        return new ResponseEntity<>(accountService.withdraw(accountNumber, withdrawalAmount), HttpStatus.OK);
    }
}
