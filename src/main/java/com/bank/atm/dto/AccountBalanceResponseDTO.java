package com.bank.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceResponseDTO {
    private BigDecimal accountBalance;
    private BigDecimal maximumLimit;
}
