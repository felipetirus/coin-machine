package com.coin_machine.coinmachine.exception;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NoChangeFoundException extends IllegalArgumentException {
    public NoChangeFoundException() {
        super(String.format("The machine couldn't find change"));
    }
}
