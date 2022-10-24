package com.bank.atm;

import com.bank.atm.dispense.CashDispenseService;
import com.bank.atm.dto.ATMDTO;
import com.bank.atm.dto.AccountBalanceResponseDTO;
import com.bank.atm.dto.WithdrawalResponseDTO;
import com.bank.atm.entity.AccountEntity;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.exception.ATMOperationException;
import com.bank.atm.repository.AccountRepository;
import com.bank.atm.repository.CustomerRepository;
import com.bank.atm.service.ATMFundsService;
import com.bank.atm.service.AccountService;
import com.bank.atm.service.AccountTransactionService;
import com.bank.atm.service.CustomerService;
import com.bank.atm.service.impl.AccountServiceImpl;
import com.bank.atm.util.enums.CashNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ATMFundsService atmFundsService;

    @InjectMocks
    private AccountService accountService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private CashDispenseService cashDispenseService;
    @MockBean
    private AccountTransactionService accountTransactionService;


    @BeforeEach
    public void setup() {
        accountRepository = Mockito.mock(AccountRepository.class);
        atmFundsService = Mockito.mock(ATMFundsService.class);
        customerService = Mockito.mock(CustomerService.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        accountService = new AccountServiceImpl(
                accountRepository,
                customerRepository,
                customerService,
                bCryptPasswordEncoder,
                atmFundsService,
                cashDispenseService,
                accountTransactionService);
    }

    @Test
    void getAccountBalance() throws ATMDataValidationException {
        AccountEntity account = new AccountEntity("123456789", "1234", BigDecimal.valueOf(800), BigDecimal.valueOf(200));
        given(accountRepository.findById("123456789")).willReturn(Optional.of(account));

        AccountBalanceResponseDTO accountBalance = accountService.getAccountBalance("123456789");

        assertAll(
                () -> assertNotNull(accountBalance),
                () -> assertEquals(new BigDecimal("800"), accountBalance.getAccountBalance()),
                () -> assertEquals(new BigDecimal("1000"), accountBalance.getAccountBalance())
        );
    }


    @Test
    void shouldThrowErrorWhenInvalidAccountNumber() {
        assertThrows(ATMDataValidationException.class, () -> accountService.getAccountBalance("123456789"));
    }

    @Test
    void shouldThrowErrorWhenWithdrawalAmountIsZero() {
        assertThrows(ATMDataValidationException.class, () -> accountService.withdraw("123456789", BigDecimal.ZERO));
    }

    @Test
    void shouldThrowErrorWhenInvalidAccountNumberIsGiven() {
        assertThrows(ATMDataValidationException.class, () -> accountService.withdraw("123456789", BigDecimal.valueOf(100)));
    }

    @Test
    void shouldThrowErrorWhenInsufficientFundsInTheAccount() {
        AccountEntity account = new AccountEntity("123456789", "1234", BigDecimal.valueOf(800), BigDecimal.valueOf(200));
        given(accountRepository.findById("123456789")).willReturn(Optional.of(account));
        assertThrows(ATMDataValidationException.class, () -> accountService.withdraw("123456789", BigDecimal.valueOf(1500)));
    }

    @Test
    void withdrawFunds() throws ATMOperationException, ATMDataValidationException {
        AccountEntity account = new AccountEntity("123456789", "1234", BigDecimal.valueOf(800), BigDecimal.valueOf(200));

        Map<CashNote, Integer> cashMap = new HashMap<>();
        cashMap.put(CashNote.FIFTY, 10);
        cashMap.put(CashNote.TWENTY, 30);
        cashMap.put(CashNote.TEN, 30);
        cashMap.put(CashNote.FIVE, 20);
        ATMDTO atmdto = new ATMDTO((long) 1, BigDecimal.valueOf(1500), cashMap);

        BigDecimal withdrawalAmount = BigDecimal.valueOf(500);

        given(accountRepository.findById("123456789")).willReturn(Optional.of(account));
        given(accountRepository.save(account)).willReturn(account);


        WithdrawalResponseDTO withdrawalResponseDTO = accountService.withdraw("123456789", withdrawalAmount);

        Map<CashNote, Integer> dispensedCashMap = new HashMap<>();
        dispensedCashMap.put(CashNote.FIFTY, 10);

        assertAll(
                () -> assertNotNull(withdrawalResponseDTO),
                () -> assertEquals(new BigDecimal("300"), withdrawalResponseDTO.getRemainingBalance()),
                () -> assertEquals(dispensedCashMap, withdrawalResponseDTO.getCashList())
        );
    }


    @Test
    void withdrawFundsWhichDispenseAllCashTypes() throws ATMOperationException, ATMDataValidationException {
        AccountEntity account = new AccountEntity("123456789", "1234", BigDecimal.valueOf(800), BigDecimal.valueOf(200));

        Map<CashNote, Integer> cashMap = new HashMap<>();
        cashMap.put(CashNote.FIFTY, 10);
        cashMap.put(CashNote.TWENTY, 30);
        cashMap.put(CashNote.TEN, 30);
        cashMap.put(CashNote.FIVE, 20);
        ATMDTO atmdto = new ATMDTO((long) 1, BigDecimal.valueOf(1500), cashMap);

        BigDecimal withdrawalAmount = BigDecimal.valueOf(935);

        given(accountRepository.findById("123456789")).willReturn(Optional.of(account));
        given(accountRepository.save(account)).willReturn(account);

        WithdrawalResponseDTO withdrawalResponseDTO = accountService.withdraw("123456789", withdrawalAmount);

        Map<CashNote, Integer> dispensedCashMap = new HashMap<>();
        dispensedCashMap.put(CashNote.FIFTY, 10);
        dispensedCashMap.put(CashNote.TWENTY, 21);
        dispensedCashMap.put(CashNote.TEN, 1);
        dispensedCashMap.put(CashNote.FIVE, 1);

        assertAll(
                () -> assertNotNull(withdrawalResponseDTO),
                () -> assertEquals(new BigDecimal("-135"), withdrawalResponseDTO.getRemainingBalance()),
                () -> assertEquals(dispensedCashMap, withdrawalResponseDTO.getCashList())
        );
    }
}
