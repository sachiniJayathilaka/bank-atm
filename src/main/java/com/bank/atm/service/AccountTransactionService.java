package com.bank.atm.service;

import com.bank.atm.dto.AccountTransactionHistoryDTO;
import com.bank.atm.entity.AccountEntity;

import java.math.BigDecimal;

public interface AccountTransactionService {

    void save(AccountEntity accountEntity, BigDecimal withdrawalAmount);

    AccountTransactionHistoryDTO getHistoryByAccountNumber(String accountNumber);

}
