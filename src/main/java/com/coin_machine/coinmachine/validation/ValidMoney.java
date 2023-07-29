package com.coin_machine.coinmachine.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target( { ElementType.TYPE_USE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MoneyValidator.class)
public @interface ValidMoney {

    MoneyType moneyType();
    public String message() default "The Bill value is invalid.";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};

    public enum MoneyType {
        NOTES, COINS
    }
}
