package com.coin_machine.coinmachine.repository;

import com.coin_machine.coinmachine.entity.MachineCoins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineCoinsRepository extends JpaRepository<MachineCoins, Integer> {
    public List<MachineCoins> findAllByOrderByAmountDesc();
}