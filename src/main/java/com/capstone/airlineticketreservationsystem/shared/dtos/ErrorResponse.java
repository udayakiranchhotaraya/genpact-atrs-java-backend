package com.capstone.airlineticketreservationsystem.shared.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class ErrorResponse {

    public ErrorResponse(int status, String message, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(int status, String message) {
        this(status, message, Instant.now());
    }

    private int status; // HTTP status code (e.g., 404, 500)
    private String message; // Error detail from the exception
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp; // When the error occurred

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
