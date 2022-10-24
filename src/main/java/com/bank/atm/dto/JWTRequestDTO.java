package com.bank.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTRequestDTO {
    private String accountNumber;
    private String pin;
}
