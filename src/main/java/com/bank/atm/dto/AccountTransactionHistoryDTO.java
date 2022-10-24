package com.bank.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransactionHistoryDTO {
    private List<AccountTransactionDTO> accountTransactionDTOS;
    private String accountNumber;
    private String customerName;
    private BigDecimal accountBalance;
}
