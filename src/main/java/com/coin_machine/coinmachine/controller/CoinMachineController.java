package com.coin_machine.coinmachine.controller;

import com.coin_machine.coinmachine.entity.MachineCoins;
import com.coin_machine.coinmachine.entity.Transaction;
import com.coin_machine.coinmachine.model.NewCoins;
import com.coin_machine.coinmachine.service.CoinMachineService;
import com.coin_machine.coinmachine.service.TransactionService;
import com.coin_machine.coinmachine.validation.ValidMoney;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/machine")
@AllArgsConstructor
@Validated
public class CoinMachineController
{

    CoinMachineService coinMachineService;
    TransactionService transactionService;

    @GetMapping(value = "/total-coins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> totalCoins() {
        log.info("Checking totalCoins");
        return ResponseEntity.ok(coinMachineService.getTotalMachine());
    }

    @PostMapping(value = "/change-bills", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MachineCoins>> changeNotes(
            @Valid
            @RequestBody
            @NotEmpty(message = "Input bills list cannot be empty")
            List<@ValidMoney(moneyType= ValidMoney.MoneyType.NOTES) Integer> listOfBills, @RequestParam(defaultValue = "false") Boolean mostCoins) {
        log.info("Changing notes");
        return ResponseEntity.ok(coinMachineService.changeBills(listOfBills, mostCoins));
    }

    @GetMapping(value = "/transaction/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        log.info("Getting transactions");
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping(value = "/add-coins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCoins(
            @Valid
            @RequestBody
            @NotEmpty(message = "Input coins list cannot be empty.")
            List<NewCoins> listOfCoins) {
        log.info("Adding coins");
        coinMachineService.addCoins(listOfCoins);
        return ResponseEntity.ok("New total of coins is "+coinMachineService.getTotalMachine());
    }

}
