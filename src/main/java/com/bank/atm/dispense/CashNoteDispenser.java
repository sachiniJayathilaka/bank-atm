package com.bank.atm.dispense;

import com.bank.atm.dto.WithdrawAmountDTO;
import com.bank.atm.util.enums.CashNote;

import java.math.BigDecimal;
import java.util.Map;

public class CashNoteDispenser implements CashDispensableChain {

    private CashNote cashNote;
    private CashDispensableChain nextChain;

    public CashNoteDispenser(CashNote cashNote) {
        this.cashNote = cashNote;
    }

    @Override
    public void setNextChain(CashDispensableChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void dispense(WithdrawAmountDTO withdrawAmountDTO, Map<CashNote, Integer> availableCashMap, Map<CashNote, Integer> dispensingCashMap) {
        int currentNoteCashValue = cashNote.getCashNoteValue();

        if (withdrawAmountDTO.getWithdrawAmount().intValue() >= currentNoteCashValue) {
            int numberOfNotes = (int) (withdrawAmountDTO.getWithdrawAmount().intValue() / currentNoteCashValue);
            int remainder = withdrawAmountDTO.getWithdrawAmount().intValue() % currentNoteCashValue;

            if (numberOfNotes > 0) {
                dispensingCashMap.put(cashNote, numberOfNotes);
            }

            if (remainder != 0) {
                this.nextChain.dispense(new WithdrawAmountDTO(new BigDecimal(remainder)), availableCashMap, dispensingCashMap);
            }
        } else {
            this.nextChain.dispense(withdrawAmountDTO, availableCashMap, dispensingCashMap);
        }
    }
}
