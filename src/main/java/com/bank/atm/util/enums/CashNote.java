package com.bank.atm.util.enums;

public enum CashNote {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50);

    private int cashNoteValue;

    CashNote(final int cashNoteValue) {
        this.cashNoteValue = cashNoteValue;
    }

    public int getCashNoteValue() {
        return this.cashNoteValue;
    }

}
