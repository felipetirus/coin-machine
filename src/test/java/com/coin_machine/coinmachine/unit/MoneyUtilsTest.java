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
                new MachineCoins(BigDecimal.valueOf(0.25), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.10), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.05), 100, null),
                new MachineCoins(BigDecimal.valueOf(0.01), 100, null)
        );
    }

    @Test
    public void totalAmountBillsTest() {
        Integer total = MoneyUtils.totalAmountBills(Arrays.asList(50, 100, 10, 5));
        assertEquals(total, 165);
    }

    @Test
    public void getAmountInCoinsGettingFromHigherTest() {
        Collections.sort(listCoins, (s1, s2) -> s2.getAmount().compareTo(s1.getAmount()));
        List<MachineCoins> listChangeCoins =  MoneyUtils.getAmountInCoins(listCoins, 5, false);
        assertNotNull(listChangeCoins);
        assertEquals(1, listChangeCoins.size());
        assertEquals(BigDecimal.valueOf(0.25), listChangeCoins.get(0).getAmount());
        assertEquals(20, listChangeCoins.get(0).getQuantity());
    }

    @Test
    public void getAmountInCoinsGettingFromLowerTest() {
        List<MachineCoins> listChangeCoins =  MoneyUtils.getAmountInCoins(listCoins, 5, true);
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
                () -> MoneyUtils.getAmountInCoins(listCoins, 100, false)
        );

        assertTrue(thrown.getMessage().contains("The machine only have 41.00 and you want 100.00."));
    }

}
