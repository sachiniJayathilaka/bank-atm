package com.bank.atm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private String errorMessage;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    public ErrorResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
        this.timestamp = new Date();
    }
}
