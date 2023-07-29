package com.coin_machine.coinmachine.service;

import com.coin_machine.coinmachine.entity.MachineCoins;
import com.coin_machine.coinmachine.entity.Transaction;
import com.coin_machine.coinmachine.exception.OutOfMoneyException;
import com.coin_machine.coinmachine.model.NewCoins;
import com.coin_machine.coinmachine.repository.MachineCoinsRepository;
import com.coin_machine.coinmachine.repository.TransactionRepository;
import com.coin_machine.coinmachine.utils.MoneyUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CoinMachineServiceImpl implements CoinMachineService {

    MachineCoinsRepository machineCoinsRepository;
    TransactionRepository transactionRepository;

    public BigDecimal getTotalMachine() {
        return MoneyUtils.getTotalAmountList(machineCoinsRepository.findAll());
    }

    public List<MachineCoins> getAvailableBankCoins() {
        return machineCoinsRepository.findAllByOrderByAmountDesc();
    }

    @Transactional
    private void removeCoinsAndInsertTransaction(List<MachineCoins> removeCoins, Transaction newTransaction) {
        transactionRepository.save(newTransaction);
        List<MachineCoins> updatedEntity = MoneyUtils.removeCoins(removeCoins, machineCoinsRepository.findAll());
        machineCoinsRepository.saveAll(updatedEntity);
    }

    public List<MachineCoins> changeBills(List<Integer> listOfBills, Boolean mostCoins) {
        BigDecimal totalAmount = MoneyUtils.totalAmountBills(listOfBills);
        List<MachineCoins> allCoins = getAvailableBankCoins();
        MoneyUtils.validateMachineAmount(totalAmount, allCoins);
        List<MachineCoins> coins = MoneyUtils.getAmountInCoins(allCoins, totalAmount, mostCoins);
        Transaction transaction = MoneyUtils.createTransaction(listOfBills, coins);
        removeCoinsAndInsertTransaction(coins, transaction);
        return coins;
    }

    public void addCoins(List<NewCoins> listCoins) {
        machineCoinsRepository.saveAll(MoneyUtils.addCoins(listCoins, machineCoinsRepository.findAll()));
    }

}
