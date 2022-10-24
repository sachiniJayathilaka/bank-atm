package com.bank.atm.dto;

import com.bank.atm.util.enums.CashNote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalResponseDTO {

    private BigDecimal remainingBalance;
    private Map<CashNote, Integer> cashList;
}
