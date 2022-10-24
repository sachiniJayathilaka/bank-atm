package com.bank.atm.service.impl;

import com.bank.atm.dto.AccountTransactionDTO;
import com.bank.atm.dto.AccountTransactionHistoryDTO;
import com.bank.atm.entity.AccountEntity;
import com.bank.atm.entity.AccountTransactionEntity;
import com.bank.atm.repository.AccountRepository;
import com.bank.atm.repository.AccountTransactionRepository;
import com.bank.atm.service.AccountTransactionService;
import com.bank.atm.util.enums.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {

    private final AccountTransactionRepository accountTransactionRepository;
    private final AccountRepository accountRepository;

    public AccountTransactionServiceImpl(AccountTransactionRepository accountTransactionRepository, AccountRepository accountRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void save(AccountEntity accountEntity, BigDecimal withdrawalAmount) {
        AccountTransactionEntity accountTransactionEntity = new AccountTransactionEntity();
        accountTransactionEntity.setAccountEntity(accountEntity);
        accountTransactionEntity.setTransactionAmount(withdrawalAmount);
        accountTransactionEntity.setTransactionType(TransactionType.WITHDRAW);
        accountTransactionEntity.setTransactionTime(new Date());
        accountTransactionRepository.save(accountTransactionEntity);
    }

    @Override
    public AccountTransactionHistoryDTO getHistoryByAccountNumber(String accountNumber) {
        AccountTransactionHistoryDTO accountTransactionHistoryDTO = new AccountTransactionHistoryDTO();
        List<AccountTransactionDTO> accountTransactionDTOS = new ArrayList<>();
        List<AccountTransactionEntity> accountTransactionEntities = accountTransactionRepository.getAccountTransactionEntityByAccountEntity_AccountNumber(accountNumber);
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        accountTransactionHistoryDTO.setAccountNumber(accountEntity.getAccountNumber());
        accountTransactionHistoryDTO.setAccountBalance(accountEntity.getOpeningBalance());
        accountTransactionHistoryDTO.setCustomerName(accountEntity.getCustomerEntity().getFirstName().concat(" ").concat(accountEntity.getCustomerEntity().getLastName()));
        if (!accountTransactionEntities.isEmpty()) {
            accountTransactionDTOS = accountTransactionEntities.stream().map(item -> {
                AccountTransactionDTO accountTransactionDTO = new AccountTransactionDTO();
                accountTransactionDTO.setTransactionAmount(item.getTransactionAmount());
                accountTransactionDTO.setTransactionType(item.getTransactionType().getTransactionType());
                accountTransactionDTO.setTransactionTime(item.getTransactionTime());
                return accountTransactionDTO;
            }).collect(Collectors.toList());
        }
        accountTransactionHistoryDTO.setAccountTransactionDTOS(accountTransactionDTOS);
        return accountTransactionHistoryDTO;
    }
}
