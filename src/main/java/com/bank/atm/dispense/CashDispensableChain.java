package com.bank.atm.dispense;

import com.bank.atm.dto.WithdrawAmountDTO;
import com.bank.atm.util.enums.CashNote;

import java.util.Map;

public interface CashDispensableChain {
    void setNextChain(CashDispensableChain nextChain);

    void dispense(WithdrawAmountDTO withdrawAmountDTO, Map<CashNote, Integer> availableCashMap, Map<CashNote, Integer> dispensedCashMap);
}
