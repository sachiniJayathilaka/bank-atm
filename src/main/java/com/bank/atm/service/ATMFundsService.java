package com.bank.atm.service;

import com.bank.atm.dto.ATMDTO;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.util.enums.CashNote;

import java.util.Map;

public interface ATMFundsService {

    ATMDTO availableFundsByATM(Long atmId) throws ATMDataValidationException;

    public void deductDispensedCashFromATMCash(Long atmId, Map<CashNote, Integer> dispensedCashMap);
}
