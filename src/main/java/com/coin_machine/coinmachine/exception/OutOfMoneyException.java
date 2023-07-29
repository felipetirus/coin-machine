package com.coin_machine.coinmachine.exception;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OutOfMoneyException extends IllegalArgumentException {
    public OutOfMoneyException(BigDecimal amountMachine, BigDecimal amountRequested) {
        super(String.format("The machine only have %.2f and you want %.2f.", amountMachine ,amountRequested));
    }
}
