package com.coin_machine.coinmachine.service;

import com.coin_machine.coinmachine.entity.MachineCoins;
import com.coin_machine.coinmachine.entity.Transaction;
import com.coin_machine.coinmachine.model.NewCoins;
import com.coin_machine.coinmachine.repository.MachineCoinsRepository;
import com.coin_machine.coinmachine.repository.TransactionRepository;
import com.coin_machine.coinmachine.utils.MoneyUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CoinMachineServiceImpl implements CoinMachineService {

    MachineCoinsRepository machineCoinsRepository;
    TransactionRepository transactionRepository;

    public BigDecimal getTotalMachine() {
        return machineCoinsRepository.findAll()
                .stream()
                .reduce(BigDecimal.ZERO, (totalAmount, note) ->
                                totalAmount.add(note.getAmount().multiply(BigDecimal.valueOf(note.getQuantity()))),
                        BigDecimal::add);
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
        Integer totalAmount = MoneyUtils.totalAmountBills(listOfBills);
        List<MachineCoins> coins = MoneyUtils.getAmountInCoins(getAvailableBankCoins(), totalAmount, mostCoins);
        Transaction transaction = MoneyUtils.createTransaction(listOfBills, coins);
        removeCoinsAndInsertTransaction(coins, transaction);
        return coins;
    }

    public void addCoins(List<NewCoins> listCoins) {
        machineCoinsRepository.saveAll(MoneyUtils.addCoins(listCoins, machineCoinsRepository.findAll()));
    }

}
