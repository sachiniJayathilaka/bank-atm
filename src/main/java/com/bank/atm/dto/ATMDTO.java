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
public class ATMDTO {
    private Long atmId;
    private BigDecimal remainingBalanceInATM;
    private Map<CashNote, Integer> cashMap;
}
