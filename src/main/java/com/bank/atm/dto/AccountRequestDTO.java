package com.bank.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {
    private String accountNumber;
    private String pin;
    private BigDecimal openingBalance;
    private BigDecimal overdraftAmount;
    private CustomerDTO customerDTO;
}
