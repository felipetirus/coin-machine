package com.coin_machine.coinmachine.exception;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class InvalidBillException extends IllegalArgumentException {

    String fieldName;

    public InvalidBillException(List<Integer> invalidBills, String fieldName) {
        super(String.format("Field: %s with Invalid bill notes: %s", fieldName, invalidBills.stream().map(Object::toString)
                .collect(Collectors.joining(", "))));
        this.fieldName = fieldName;
    }
}
