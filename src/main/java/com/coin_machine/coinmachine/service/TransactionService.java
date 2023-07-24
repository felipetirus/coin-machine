package com.coin_machine.coinmachine.service;

import com.coin_machine.coinmachine.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

}
