package com.coin_machine.coinmachine.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

public class MoneyValidator implements ConstraintValidator<ValidMoney, Integer> {

    ValidMoney.MoneyType moneyType;

    String message;

    @Value("${bank.notes}")
    private Integer[] validBills;

    @Value("${bank.coins}")
    private Integer[] validCoins;

    @Override
    public void initialize(ValidMoney constraintAnnotation){
        moneyType = constraintAnnotation.moneyType();
    }
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ValidMoney.MoneyType.NOTES.equals(moneyType) ? "Invalid note": "Invalid coin").addConstraintViolation();

        if (moneyType.equals(ValidMoney.MoneyType.NOTES)) {
            return  Arrays.asList(this.validBills).contains(value);
        }
        return  Arrays.asList(this.validCoins).contains(value);
    }


}