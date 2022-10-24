package com.bank.atm.service.impl;

import com.bank.atm.dispense.CashDispenseService;
import com.bank.atm.dto.*;
import com.bank.atm.entity.AccountEntity;
import com.bank.atm.entity.CustomerEntity;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.exception.ATMOperationException;
import com.bank.atm.repository.AccountRepository;
import com.bank.atm.repository.CustomerRepository;
import com.bank.atm.service.ATMFundsService;
import com.bank.atm.service.AccountService;
import com.bank.atm.service.AccountTransactionService;
import com.bank.atm.service.CustomerService;
import com.bank.atm.util.enums.CashNote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ATMFundsService atmFundsService;
    private final CashDispenseService cashDispenseService;
    private final AccountTransactionService accountTransactionService;

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository, CustomerService customerService, BCryptPasswordEncoder bCryptPasswordEncoder, ATMFundsService atmFundsService, CashDispenseService cashDispenseService, AccountTransactionService accountTransactionService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.atmFundsService = atmFundsService;
        this.cashDispenseService = cashDispenseService;
        this.accountTransactionService = accountTransactionService;
    }

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new UsernameNotFoundException(accountNumber + " account Number does not exist!");
        }
        return new User(accountEntity.getAccountNumber(), accountEntity.getPin(), new ArrayList<>());
    }

    @Transactional
    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) throws ATMDataValidationException {
        log.info("validateRequiredFields for validate fields {} ", accountRequestDTO);
        validateRequiredFields(accountRequestDTO);

        AccountEntity duplicateAccount = accountRepository.findByAccountNumber(accountRequestDTO.getAccountNumber());
        if (duplicateAccount != null) {
            throw new ATMDataValidationException(accountRequestDTO.getAccountNumber() + " account already exist!");
        }
        Long customerId = customerService.saveNewCustomer(accountRequestDTO.getCustomerDTO());
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(accountRequestDTO.getAccountNumber());
        accountEntity.setOpeningBalance(accountRequestDTO.getOpeningBalance());
        accountEntity.setOverdraftAmount(accountRequestDTO.getOverdraftAmount());
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new ATMDataValidationException("Customer does not Exist.");
        }
        accountEntity.setCustomerEntity(customer.get());
        log.info("encrypt the pin", bCryptPasswordEncoder.encode(accountRequestDTO.getPin()));
        accountEntity.setPin(bCryptPasswordEncoder.encode(accountRequestDTO.getPin()));

        AccountEntity createdEntity = accountRepository.save(accountEntity);

        AccountResponseDTO createdAccountDTO = new AccountResponseDTO();
        createdAccountDTO.setAccountNumber(createdEntity.getAccountNumber());
        return createdAccountDTO;
    }

    @Override
    public AccountBalanceResponseDTO getAccountBalance(String accountNumber) throws ATMDataValidationException {
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountNumber);
        if (!accountEntity.isPresent()) {
            throw new ATMDataValidationException("Invalid Account Number.");
        }
        AccountBalanceResponseDTO accountBalanceResponseDTO = new AccountBalanceResponseDTO();
        accountBalanceResponseDTO.setAccountBalance(accountEntity.get().getOpeningBalance());
        accountBalanceResponseDTO.setMaximumLimit(accountEntity.get().getOpeningBalance().add(accountEntity.get().getOverdraftAmount()));
        return accountBalanceResponseDTO;
    }

    private void validateRequiredFields(AccountRequestDTO accountRequestDTO) throws ATMDataValidationException {
        if (accountRequestDTO.getAccountNumber() == null) {
            throw new ATMDataValidationException("Account Number is Required.");
        }
        if (accountRequestDTO.getOpeningBalance() == null) {
            throw new ATMDataValidationException("Account Opening Balance Should be Entered.");
        }
        if (accountRequestDTO.getPin() == null) {
            throw new ATMDataValidationException("PIN Number is Required.");
        }
        if (accountRequestDTO.getCustomerDTO() == null) {
            throw new ATMDataValidationException("Customer Details are Required.");
        }
        if (accountRequestDTO.getCustomerDTO().getFirstName() == null) {
            throw new ATMDataValidationException("Customer First Name is Required.");
        }
        if (accountRequestDTO.getCustomerDTO().getLastName() == null) {
            throw new ATMDataValidationException("Customer Last Name is Required.");
        }
    }

    public String getAuthenticatedAccountNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public WithdrawalResponseDTO withdraw(String accountNumber, BigDecimal withdrawalAmount)
            throws ATMDataValidationException, ATMOperationException {
        Long atmId = Long.valueOf(1);
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountNumber);
        if (!accountEntity.isPresent()) {
            throw new ATMDataValidationException("Invalid Account Number.");
        } else if (withdrawalAmount == null) {
            throw new ATMDataValidationException("Withdrawal Amount Should be Entered.");
        } else if (withdrawalAmount.intValue() < cashDispenseService.getMinimumCashNoteValue()) {
            throw new ATMDataValidationException("Minimum withdrawal amount is " + cashDispenseService.getMinimumCashNoteValue());
        } else if (withdrawalAmount.remainder(new BigDecimal(cashDispenseService.getMinimumCashNoteValue())).intValue() != 0) {
            throw new ATMDataValidationException("Withdrawal amount must be in multiples of " + cashDispenseService.getMinimumCashNoteValue());
        }
        // validate withdrawal amount with the available funds in the account
        if (accountEntity.get().getOpeningBalance().add(accountEntity.get().getOverdraftAmount()).compareTo(withdrawalAmount) < 0) {
            log.error("No Sufficient Funds Available in the Account.");
            throw new ATMDataValidationException("No Sufficient Funds Available in the Account.");
        }

        ATMDTO availableFunds = atmFundsService.availableFundsByATM(atmId);
        if (availableFunds.getRemainingBalanceInATM().doubleValue() == 0) {
            log.error("No Funds Available in the ATM");
            throw new ATMOperationException("No Funds Available in the ATM.");
        }

        if (withdrawalAmount.compareTo(availableFunds.getRemainingBalanceInATM()) > 0) {
            log.error("No Sufficient Funds Available in the ATM.");
            throw new ATMOperationException("No Sufficient Funds Available in the ATM.");
        }

        Map<CashNote, Integer> dispensingCashMap = new HashMap<>();
        WithdrawAmountDTO withdrawAmountDTO = new WithdrawAmountDTO();
        withdrawAmountDTO.setWithdrawAmount(withdrawalAmount);
        dispensingCashMap = cashDispenseService.dispenseCash(withdrawAmountDTO, availableFunds.getCashMap());

        //debit from the account
        accountEntity.get().setOpeningBalance(accountEntity.get().getOpeningBalance().subtract(withdrawalAmount));
        AccountEntity persistAccount = accountRepository.save(accountEntity.get());

        //update transaction table
        accountTransactionService.save(persistAccount, withdrawalAmount);

        //deduct from the atm cash
        atmFundsService.deductDispensedCashFromATMCash(availableFunds.getAtmId(), dispensingCashMap);

        WithdrawalResponseDTO withdrawalResponseDTO = new WithdrawalResponseDTO();
        withdrawalResponseDTO.setCashList(dispensingCashMap);
        withdrawalResponseDTO.setRemainingBalance(accountEntity.get().getOpeningBalance().subtract(withdrawalAmount));
        return withdrawalResponseDTO;
    }

}
