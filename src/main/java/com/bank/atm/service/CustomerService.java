package com.bank.atm.service;

import com.bank.atm.dto.CustomerDTO;
import com.bank.atm.exception.ATMDataValidationException;

public interface CustomerService {
    public Long saveNewCustomer(CustomerDTO customerDTO) throws ATMDataValidationException;
}
