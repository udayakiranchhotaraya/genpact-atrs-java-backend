package com.capstone.airlineticketreservationsystem.authentication.dtos;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Email or phone number is required")
    private String loginId; // Can be email or phone number

    @NotBlank(message = "Password is required")
    private String password;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}