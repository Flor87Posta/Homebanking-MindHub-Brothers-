package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardType;

public class PaymentDTO {

    private String number;
    private int cvv;
    private double amount;
    private String description;
    private CardType typeCard;
    private String email;

    public PaymentDTO() {
    }

    //getter y setter:
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PaymentDTO(String number, int cvv, double amount, String description, CardType typeCard, String email){
        this.number=number;
        this.cvv=cvv;
        this.description=description;
        this.typeCard=typeCard;
        this.email=email;

    }
}
