package com.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class AccountEntity {

    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "pin")
    private String pin;

    @Column(name = "opening_balance")
    private BigDecimal openingBalance;

    @Column(name = "overdraft_amount")
    private BigDecimal overdraftAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customerEntity;

    public AccountEntity(String accountNumber, String pin, BigDecimal openingBalance, BigDecimal overdraftAmount) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.openingBalance = openingBalance;
        this.overdraftAmount = overdraftAmount;
    }

}
