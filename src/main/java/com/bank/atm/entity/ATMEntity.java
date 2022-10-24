package com.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "atm")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ATMEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atmEntity")
    private List<ATMFundsEntity> ATMCashCollection;


}
