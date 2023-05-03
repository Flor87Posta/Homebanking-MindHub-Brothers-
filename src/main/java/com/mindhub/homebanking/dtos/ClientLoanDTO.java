package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.stream.Collectors;

public class ClientLoanDTO {

    private long id;
    private long  loanId;
    private String name;
    private double amount;
    private int payments;

    public ClientLoanDTO(){} //no se usan los constructores DTO en este caso porq solo enviamos la info, si tuvieramos que construir el objeto si

    public ClientLoanDTO(ClientLoan clientLoan) {

        this.id = clientLoan.getId();

        this.loanId = clientLoan.getId();

        this.name = clientLoan.getLoan().name;

        this.amount = clientLoan.getAmount();

        this.payments = clientLoan.getPayments();

    }

//Métodos accesores:

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }




}
