package com.coin_machine.coinmachine.entity;

import com.coin_machine.coinmachine.validation.ValidMoney;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineCoins {
    @Id
    private BigDecimal amount;
    private Integer quantity;
    @Version
    private LocalDateTime version;

    public void subtractCoins(Integer quantity) {
        this.quantity -= quantity;

    }

    public void addCoins(Integer quantity) {
        this.quantity += quantity;

    }
}
