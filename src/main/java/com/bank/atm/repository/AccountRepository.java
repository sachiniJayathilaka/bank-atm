package com.bank.atm.repository;

import com.bank.atm.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    AccountEntity findByAccountNumber(String accountNumber);
}
