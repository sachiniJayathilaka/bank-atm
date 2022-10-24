package com.bank.atm.controller;

import com.bank.atm.dto.JWTRequestDTO;
import com.bank.atm.dto.JWTResponseDTO;
import com.bank.atm.service.AccountService;
import com.bank.atm.util.JWTUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/login/")
@RestController
@CrossOrigin
public class LoginController {
    private final JWTUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    public LoginController(JWTUtility jwtUtility, AuthenticationManager authenticationManager, AccountService accountService) {
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTResponseDTO> authenticate(@RequestBody JWTRequestDTO jwtRequestDTO) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequestDTO.getAccountNumber(),
                            jwtRequestDTO.getPin()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad Credentials");
        }

        final UserDetails userDetails
                = accountService.loadUserByUsername(jwtRequestDTO.getAccountNumber());

        final String token =
                jwtUtility.generateToken(userDetails);
        return new ResponseEntity<>(new JWTResponseDTO(token), HttpStatus.OK);
    }
}
