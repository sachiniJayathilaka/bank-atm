package com.bank.atm.service.impl;

import com.bank.atm.dto.CustomerDTO;
import com.bank.atm.entity.CustomerEntity;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.repository.CustomerRepository;
import com.bank.atm.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Long saveNewCustomer(CustomerDTO customerDTO) throws ATMDataValidationException {
        if (customerDTO.getFirstName() == null) {
            throw new ATMDataValidationException("Customer First Name is Required.");
        }
        if (customerDTO.getLastName() == null) {
            throw new ATMDataValidationException("Customer Last Name is Required.");
        }

        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        return customerRepository.save(customer).getId();
    }
}
