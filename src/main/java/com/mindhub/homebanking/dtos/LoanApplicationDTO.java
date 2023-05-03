package com.mindhub.homebanking.dtos;



public class LoanApplicationDTO {
    private String  loanId; //es para el tipo de préstamo q elige, el preaprobado
    private double amount;
    private int payments;
    private String destinationAccNumber;

    public LoanApplicationDTO(){} //constructor vacío para que cuando se envíe el objeto desde el front pueda crearlos
    //el otro constructor con las propiedades no haría falta porque no vamos a enviar info?
    //este DTO es creado solo para recibir info , no para enviar info

//métodos accesores:

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
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

    public String getDestinationAccNumber() {
        return destinationAccNumber;
    }

    public void setDestinationAccNumber(String destinationAccNumber) {
        this.destinationAccNumber = destinationAccNumber;
    }
}

