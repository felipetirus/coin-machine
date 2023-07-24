package com.coin_machine.coinmachine.utils;

import com.coin_machine.coinmachine.entity.MachineCoins;
import com.coin_machine.coinmachine.entity.Transaction;
import com.coin_machine.coinmachine.exception.OutOfMoneyException;
import com.coin_machine.coinmachine.model.NewCoins;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
public class MoneyUtils {

    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static Integer totalAmountBills(List<Integer> listofBills) {
        return listofBills.stream().reduce(0, (a, b) -> a + b);
    }

    public static List<MachineCoins> getAmountInCoins(List<MachineCoins> listCoins, Integer amount) {
        BigDecimal amountCalc = BigDecimal.valueOf(amount);
        List<MachineCoins> returnAmount = new ArrayList<>();
        for (MachineCoins bankNote: listCoins) {
            if (bankNote.getQuantity() > 0 && amountCalc.compareTo(bankNote.getAmount()) >= 0) {
                int neededNotes = amountCalc.divide(bankNote.getAmount()).intValue();
                int availableNotes = numAvailableNotes(neededNotes, bankNote);
                amountCalc = amountCalc.subtract(bankNote.getAmount().multiply(BigDecimal.valueOf(availableNotes))) ;
                returnAmount.add(new MachineCoins(bankNote.getAmount(), availableNotes, LocalDateTime.now()));
            }
            if(amountCalc.compareTo(listCoins.get(listCoins.size()-1).getAmount()) < 0){
                break;
            }
        }
        if (amountCalc.compareTo(BigDecimal.ZERO) != 0) {
            log.error("Out of money Exception");
            throw new OutOfMoneyException(BigDecimal.valueOf(amount), amountCalc);
        }
        return returnAmount;
    }

    public static Integer numAvailableNotes(Integer neededNoted, MachineCoins currentNotes) {
        return neededNoted < currentNotes.getQuantity() ? neededNoted : currentNotes.getQuantity();
    }

    public static Transaction createTransaction(List<Integer> listOfBills, List<MachineCoins> coins) {
        listOfBills.sort(Comparator.naturalOrder());
        Map<BigDecimal, Integer> listCoins = coins.stream()
                .collect(Collectors.toMap(MachineCoins::getAmount, MachineCoins::getQuantity));
        return Transaction
                .builder()
                .listBills(listOfBills)
                .date(LocalDateTime.now())
                .listCoins(listCoins).build();
    }

    public static List<MachineCoins> removeCoins(List<MachineCoins> removeCoins, List<MachineCoins> entityCoins) {
        Map<BigDecimal, MachineCoins> removeNotesMap = removeCoins.stream()
                .collect(Collectors.toMap(MachineCoins::getAmount, Function.identity()));

        for (MachineCoins entity: entityCoins) {
            if(removeNotesMap.containsKey(entity.getAmount()))
                entity.subtractCoins(removeNotesMap.get(entity.getAmount()).getQuantity());
        }
        return entityCoins;
    }

    public static List<MachineCoins> addCoins(List<NewCoins> removeCoins, List<MachineCoins> entityCoins) {
        Map<BigDecimal, NewCoins> removeNotesMap = removeCoins.stream()
                .collect(Collectors.toMap(a-> moneyToBigDecimal(a.getAmount()), Function.identity()));

        for (MachineCoins entity: entityCoins) {
            if(removeNotesMap.containsKey(entity.getAmount()))
                entity.addCoins(removeNotesMap.get(entity.getAmount()).getQuantity());
        }
        return entityCoins;
    }

    public static BigDecimal moneyToBigDecimal(Integer value){
        return BigDecimal.valueOf(value).divide(HUNDRED);
    }

}
