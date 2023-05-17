package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    void saveNewTransaction(Transaction transaction);

    List<Transaction>findByCreatedBetweenDates(Client client, String string, LocalDateTime date, LocalDateTime date2);




}
