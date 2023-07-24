package com.coin_machine.coinmachine.service;

import com.coin_machine.coinmachine.entity.MachineCoins;
import com.coin_machine.coinmachine.model.NewCoins;

import java.math.BigDecimal;
import java.util.List;

public interface CoinMachineService {

    BigDecimal getTotalMachine();
    List<MachineCoins> getAvailableBankCoins();

    List<MachineCoins> changeBills(List<Integer> listOfBills);

    void addCoins(List<NewCoins> listCoins);

}