package com.coin_machine.coinmachine.exception;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OutOfMoneyException extends IllegalArgumentException {
    public OutOfMoneyException(BigDecimal amountRequested, BigDecimal amountLeft) {
        super(String.format("The machine only have %.2f and you want %.2f.", amountRequested.subtract(amountLeft) ,amountRequested));
    }
}
