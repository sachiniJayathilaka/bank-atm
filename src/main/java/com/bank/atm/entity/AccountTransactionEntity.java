package com.bank.atm.entity;

import com.bank.atm.util.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_transaction")
public class AccountTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_no", nullable = false)
    private AccountEntity accountEntity;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Date transactionTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;


}
