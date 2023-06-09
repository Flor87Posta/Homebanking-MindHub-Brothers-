package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts();
    AccountDTO getAccount(Long id);
    Account findById(Long id);
    Account findByNumber(String number);

    boolean existsByNumber(String number);

    void saveNewAccount(Account account);

    List<Account> findByClient(Client client);

}
