package com.capstone.airlineticketreservationsystem.authentication.controllers;

import com.capstone.airlineticketreservationsystem.authentication.dtos.LoginRequest;
import com.capstone.airlineticketreservationsystem.authentication.dtos.LoginResponse;
import com.capstone.airlineticketreservationsystem.authentication.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    private final AuthenticationService authService;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticate(loginRequest);
        return  new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
