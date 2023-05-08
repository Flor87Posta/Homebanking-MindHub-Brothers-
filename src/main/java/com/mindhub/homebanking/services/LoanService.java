package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    Loan findById(long id);
    void saveNewLoan(Loan loan);

    List<LoanDTO> getLoans();

}
