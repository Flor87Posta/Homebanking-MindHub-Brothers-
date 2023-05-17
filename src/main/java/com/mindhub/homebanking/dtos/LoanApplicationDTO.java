package com.mindhub.homebanking.dtos;



public class LoanApplicationDTO {
    private long  loanId; //es para el tipo de préstamo q elige, el preaprobado, lo elige por input o select...
    private double amount;

    private int payments;
    private String destinationAccNumber;

    public LoanApplicationDTO(){} //constructor vacío para que cuando se envíe el objeto desde el front pueda crearlos
    //este DTO es creado solo para recibir info , no para enviar info

    public LoanApplicationDTO(long loanId, double amount, int payments, String destinationAccNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccNumber = destinationAccNumber;
    }

//métodos accesores:


    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
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

