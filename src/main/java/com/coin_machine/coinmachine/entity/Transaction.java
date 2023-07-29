package com.coin_machine.coinmachine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime date;
    private List<Integer> listBills;

    @ElementCollection
    @CollectionTable(name = "transaction_coin_mapping",
            joinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "item_coins")
    @Column(name = "coins")
    private Map<BigDecimal, Integer> listCoins;
}