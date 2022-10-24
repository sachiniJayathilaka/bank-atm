package com.bank.atm.repository;

import com.bank.atm.entity.AccountTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransactionEntity, Long> {

    List<AccountTransactionEntity> getAccountTransactionEntityByAccountEntity_AccountNumber(@Param("accountNumber") String accountNumber);
}
