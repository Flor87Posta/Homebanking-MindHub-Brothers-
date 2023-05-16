package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardType;

public class PaymentDTO {

    private String number;
    private int cvv;
    private double amount;
    private String description;
    private CardType typeCard;

    public PaymentDTO(){};

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CardType getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(CardType typeCard) {
        this.typeCard = typeCard;
    }

    public PaymentDTO(String number, int cvv, double amount, String description, CardType typeCard){
        this.number=number;
        this.cvv=cvv;
        this.description=description;
        this.typeCard=typeCard;


        //getter y setter:




    }
}
