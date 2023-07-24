package com.coin_machine.coinmachine.exception;

import com.coin_machine.coinmachine.model.ErrorDetails;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({InvalidBillException.class, OutOfMoneyException.class})
    public ResponseEntity<ErrorDetails> handleIllegalArgumentExceptionException(
            IllegalArgumentException ex) {
        return new ResponseEntity<>(new ErrorDetails(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<List<ErrorDetails>> handleConstrainValidationException(
            ConstraintViolationException ex) {
        log.debug(
                "Constraint violation exception encountered: {}",
                ex.getConstraintViolations(),
                ex
        );

        List<ErrorDetails> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ErrorDetails(violation))
                .collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

}