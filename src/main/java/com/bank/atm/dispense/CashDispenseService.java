package com.bank.atm.dispense;

import com.bank.atm.dto.WithdrawAmountDTO;
import com.bank.atm.util.enums.CashNote;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Data
@Service
public class CashDispenseService {

    private int minimumCashNoteValue;
    private CashNoteDispenser highestCahNoteDispenser;

    public CashDispenseService() {
        // initialize the chain
        CashNoteDispenser c50NoteDispenser = new CashNoteDispenser(CashNote.FIFTY);
        CashNoteDispenser c20NoteDispenser = new CashNoteDispenser(CashNote.TWENTY);
        CashNoteDispenser c10NoteDispenser = new CashNoteDispenser(CashNote.TEN);
        CashNoteDispenser c5NoteDispenser = new CashNoteDispenser(CashNote.FIVE);

        // set the chain of responsibility
        c50NoteDispenser.setNextChain(c20NoteDispenser);
        c20NoteDispenser.setNextChain(c10NoteDispenser);
        c10NoteDispenser.setNextChain(c5NoteDispenser);

        highestCahNoteDispenser = c50NoteDispenser;
        this.minimumCashNoteValue = Stream.of(CashNote.values()).mapToInt(v -> v.getCashNoteValue()).min().getAsInt();
    }

    public Map<CashNote, Integer> dispenseCash(WithdrawAmountDTO withdrawAmountDTO, Map<CashNote, Integer> availableCashMap) {

        Map<CashNote, Integer> dispensingCashMap = new HashMap<>();

        // each member of the cash dispensing chain will update the dispensing cash map.
        highestCahNoteDispenser.dispense(withdrawAmountDTO, availableCashMap, dispensingCashMap);

        return dispensingCashMap;
    }

    public int getMinimumCashNoteValue() {
        return this.minimumCashNoteValue;
    }


}
