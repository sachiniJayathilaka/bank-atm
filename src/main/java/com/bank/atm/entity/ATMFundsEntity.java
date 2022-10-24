package com.bank.atm.entity;

import com.bank.atm.util.enums.CashNote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "atm_funds")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ATMFundsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "cash_note")
    private CashNote cashNote;

    @Column(name = "no_of_cash_notes")
    private Integer noOfCashNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atm_id")
    private ATMEntity atmEntity;
}
