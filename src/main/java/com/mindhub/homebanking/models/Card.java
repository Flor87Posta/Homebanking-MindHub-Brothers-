package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;

    private String cardholder; //concatenar nombre y apellido del cliente acá

    private CardType typeCard;

    private CardColor color;

    private String number; //va con separadores cada 4 numeros (son 12 numeros en total)

    private int cvv;

    private LocalDate thruDate; //fecha actual + 5 años

    private LocalDate fromDate; //fecha actual

    private boolean hidden; //para ocultar la tarjeta si el cliente desea eliminarla

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card(){}

    public Card (String cardholder, CardType typeCard , CardColor color, String number, int cvv, LocalDate thruDate, LocalDate fromDate, boolean hidden )   {
        this.cardholder = cardholder;
        this.typeCard = typeCard;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.hidden= hidden;
    }

    //Métodos accesores:


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(CardType typeCard) {
        this.typeCard = typeCard;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

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

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }
//métodos creados :

    public boolean isExpired() {
        LocalDate now = LocalDate.now();
        return now.isAfter(this.thruDate) || now.isEqual(this.thruDate);
    }

}
