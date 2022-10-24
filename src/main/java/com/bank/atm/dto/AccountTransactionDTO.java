package com.bank.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransactionDTO {
    private BigDecimal transactionAmount;
    private Date transactionTime;
    private String transactionType;
}
