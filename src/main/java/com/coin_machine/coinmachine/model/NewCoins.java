package com.coin_machine.coinmachine.model;

import com.coin_machine.coinmachine.validation.ValidMoney;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewCoins {
    @ValidMoney(moneyType= ValidMoney.MoneyType.COINS)
    private Integer amount;
    private Integer quantity;
}
