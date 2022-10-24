package com.bank.atm.repository;

import com.bank.atm.entity.ATMFundsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ATMFundsRepository extends JpaRepository<ATMFundsEntity, Long> {
    Set<ATMFundsEntity> findByAtmEntity_Id(@Param("id") Long id);
}
