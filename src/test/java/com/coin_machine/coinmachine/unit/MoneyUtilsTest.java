package com.coin_machine.coinmachine.unit;

import com.coin_machine.coinmachine.entity.MachineCoins;
import com.coin_machine.coinmachine.exception.OutOfMoneyException;
import com.coin_machine.coinmachine.utils.MoneyUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyUtilsTest {

    static List<MachineCoins> listCoins;
    @BeforeAll
    static void setUp() {
        listCoins = Arrays.asList(
                new MachineCoins(BigDecimal.valueOf(0.34), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.33), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.25), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.10), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.05), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.01), 100, null)
        );
    }

    @Test
    public void totalAmountBillsTest() {
        BigDecimal total = MoneyUtils.totalAmountBills(Arrays.asList(50, 100, 10, 5));
        assertEquals(total, BigDecimal.valueOf(165));
    }

    @Test
    public void getAmountInCoinsGettingFromHigherTest() {
        Collections.sort(listCoins, (s1, s2) -> s2.getAmount().compareTo(s1.getAmount()));
        List<MachineCoins> listChangeCoins =  MoneyUtils.getAmountInCoins(listCoins, BigDecimal.valueOf(5), false);
        assertNotNull(listChangeCoins);
        assertEquals(3, listChangeCoins.size());
        assertEquals(BigDecimal.valueOf(0.34), listChangeCoins.get(0).getAmount());
        assertEquals(14, listChangeCoins.get(0).getQuantity());

        assertEquals(BigDecimal.valueOf(0.1), listChangeCoins.get(1).getAmount());
        assertEquals(2, listChangeCoins.get(1).getQuantity());

        assertEquals(BigDecimal.valueOf(0.01), listChangeCoins.get(2).getAmount());
        assertEquals(4, listChangeCoins.get(2).getQuantity());
    }

    @Test
    public void getAmountInCoinsGettingFromLowerTest() {
        List<MachineCoins> listChangeCoins =  MoneyUtils.getAmountInCoins(listCoins, BigDecimal.valueOf(5), true);
        assertNotNull(listChangeCoins);
        assertEquals(2, listChangeCoins.size());
        assertEquals(BigDecimal.valueOf(0.01), listChangeCoins.get(0).getAmount());
        assertEquals(100, listChangeCoins.get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(0.05), listChangeCoins.get(1).getAmount());
        assertEquals(80, listChangeCoins.get(1).getQuantity());
    }
    @Test
    public void getAmountInCoinsTestError() {

        OutOfMoneyException thrown = assertThrows(
                OutOfMoneyException.class,
                () -> MoneyUtils.validateMachineAmount(BigDecimal.valueOf(150), listCoins)
        );

        assertEquals("The machine only have 108.00 and you want 150.00.", thrown.getMessage());
    }

}
