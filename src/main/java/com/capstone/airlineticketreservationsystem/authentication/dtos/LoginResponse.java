package com.capstone.airlineticketreservationsystem.authentication.dtos;

public class LoginResponse {

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;
    private String tokenType = "Bearer";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
