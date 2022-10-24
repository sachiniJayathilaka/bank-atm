package com.bank.atm.service;

import com.bank.atm.dto.AccountBalanceResponseDTO;
import com.bank.atm.dto.AccountRequestDTO;
import com.bank.atm.dto.AccountResponseDTO;
import com.bank.atm.dto.WithdrawalResponseDTO;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.exception.ATMOperationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;

public interface AccountService extends UserDetailsService {

    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) throws ATMDataValidationException;

    AccountBalanceResponseDTO getAccountBalance(String accountNumber) throws ATMDataValidationException;

    String getAuthenticatedAccountNumber();

    WithdrawalResponseDTO withdraw(String accountNumber, BigDecimal withdrawAmount) throws ATMDataValidationException, ATMOperationException;
}
