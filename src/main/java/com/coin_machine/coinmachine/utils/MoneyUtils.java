package com.coin_machine.coinmachine.utils;

import com.coin_machine.coinmachine.entity.MachineCoins;
import com.coin_machine.coinmachine.entity.Transaction;
import com.coin_machine.coinmachine.exception.NoChangeFoundException;
import com.coin_machine.coinmachine.exception.OutOfMoneyException;
import com.coin_machine.coinmachine.model.NewCoins;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
public class MoneyUtils {

    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static BigDecimal totalAmountBills(List<Integer> listofBills) {
        return  BigDecimal.valueOf(listofBills.stream().reduce(0, (a, b) -> a + b));
    }

    public static List<MachineCoins> getAmountInCoins(List<MachineCoins> listCoins, BigDecimal totalAmount, Boolean mostCoins) {
        List<MachineCoins> returnAmount = null;
        if(mostCoins) {
            Collections.reverse(listCoins);
        }
        BigDecimal total = null;
        while (!listCoins.isEmpty()) {
            returnAmount = findCombination(listCoins, totalAmount);
            total = getTotalAmountList(returnAmount);
            if (total.compareTo(totalAmount) == 0) {
                break;
            }
            removeCoinList(listCoins, returnAmount);
        }

        if (total.compareTo(totalAmount) != 0) {
            log.error("No combination found!");
            throw new NoChangeFoundException();
        }
        return returnAmount;
    }

    private static void removeCoinList(List<MachineCoins> listCoins, List<MachineCoins> currentList) {
        listCoins.get(0).setQuantity(currentList.get(0).getQuantity() -1);
        if (listCoins.get(0).getQuantity() <= 0) {
            listCoins.remove(0);
        }
    }

    private static List<MachineCoins> findCombination(List<MachineCoins> listCoins, BigDecimal totalAmount) {
        List<MachineCoins> returnAmount = new ArrayList<>();
        for (MachineCoins bankCoins: listCoins) {
            if (bankCoins.getQuantity() > 0 && totalAmount.compareTo(bankCoins.getAmount()) >= 0) {
                int neededCoins = totalAmount.divide(bankCoins.getAmount(), RoundingMode.DOWN).intValue();
                int availableNotes = numAvailableCoins(neededCoins, bankCoins);
                totalAmount = totalAmount.subtract(bankCoins.getAmount().multiply(BigDecimal.valueOf(availableNotes))) ;
                returnAmount.add(new MachineCoins(bankCoins.getAmount(), availableNotes, LocalDateTime.now()));
            }
            if(totalAmount.compareTo(listCoins.get(listCoins.size()-1).getAmount()) < 0){
                break;
            }
        }
        return returnAmount;
    }

    public static Integer numAvailableCoins(Integer neededCoins, MachineCoins currentCoins) {
        return neededCoins < currentCoins.getQuantity() ? neededCoins : currentCoins.getQuantity();
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

    public static BigDecimal getTotalAmountList(List<MachineCoins> allCoinsMachine) {
       return allCoinsMachine.stream()
                .reduce(BigDecimal.ZERO, (totalAmount, note) ->
                                totalAmount.add(note.getAmount().multiply(BigDecimal.valueOf(note.getQuantity()))),
                        BigDecimal::add);
    }

    public static void validateMachineAmount(BigDecimal totalAmount, List<MachineCoins> allCoins) {
        BigDecimal totalMachine = MoneyUtils.getTotalAmountList(allCoins);
        if (totalAmount.compareTo(totalMachine) > 0) {
            log.error("Out of money Exception");
            throw new OutOfMoneyException(totalMachine, totalAmount);
        }
    }

}
