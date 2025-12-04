package com.capstone.airlineticketreservationsystem.shared.dtos;

import java.time.Instant;
import java.util.Map;

public class ValidationErrorResponse extends ErrorResponse {

    public ValidationErrorResponse(int status, String message, Map<String, String> fieldErrors) {
        super(status, message, Instant.now());
        this.fieldErrors = fieldErrors;
    }

    private Map<String, String> fieldErrors;

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
