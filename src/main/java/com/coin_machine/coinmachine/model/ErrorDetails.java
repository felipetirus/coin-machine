package com.coin_machine.coinmachine.model;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;

    public ErrorDetails(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public ErrorDetails(ConstraintViolation violation) {
        this.timestamp = LocalDateTime.now();
        this.message = violation.getPropertyPath() + ": "
                + violation.getMessage()+ ": "+violation.getInvalidValue();
    }
}