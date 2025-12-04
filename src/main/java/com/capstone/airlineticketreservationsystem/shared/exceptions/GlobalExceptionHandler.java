package com.capstone.airlineticketreservationsystem.shared.exceptions;

import com.capstone.airlineticketreservationsystem.shared.dtos.ErrorResponse;
import com.capstone.airlineticketreservationsystem.shared.dtos.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

//@Order(Ordered.HIGHEST_PRECEDENCE) // Ensures this handler runs first
@RestControllerAdvice // Combines @ControllerAdvice and @ResponseBody
public class GlobalExceptionHandler {

    // Handler for Field Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        // Building a detailed error response with field-specific messages
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        // ValidationErrorResponse that extends ErrorResponse
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for the request",
                fieldErrors // Include the map of field errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class) // Catches ALL exceptions
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {

        // 1. Getting the HTTP Status from the exception's @ResponseStatus annotation
        HttpStatus status = getHttpStatusFromException(exception);

        // 2. Building the standardized ErrorResponse
        // Using the exception's message is common. You can customize this.
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                exception.getMessage()
        );

        // 3. Return the ResponseEntity with the correct status and body
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Helper method to extract HttpStatus from the exception.
     * Checks for @ResponseStatus annotation, defaults to INTERNAL_SERVER_ERROR (500).
     */
    private HttpStatus getHttpStatusFromException(Exception exception) {
        // Check if the exception class has the @ResponseStatus annotation
        ResponseStatus responseStatusAnnotation =
                exception.getClass().getAnnotation(ResponseStatus.class);

        if (responseStatusAnnotation != null) {
            // Return the code defined in the annotation (e.g., NOT_FOUND for FlightNotFoundException)
            return responseStatusAnnotation.value();
        }
        // Default for any other unexpected exception
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}