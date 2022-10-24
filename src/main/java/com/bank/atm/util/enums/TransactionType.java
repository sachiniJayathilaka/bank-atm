package com.bank.atm.util.enums;

public enum TransactionType {
    WITHDRAW("WITHDRAW");

    private String transactionType;

    TransactionType(final String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }
}
