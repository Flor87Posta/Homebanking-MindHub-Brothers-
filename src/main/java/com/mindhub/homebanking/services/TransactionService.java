package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {

    void saveNewTransaction(Transaction transaction);



}
