package com.mindhub.homebanking.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch= FetchType.EAGER) //asociado a cuenta, definido en la clase Transaction
    private Set<Transaction> transactions = new HashSet<>(); // set para evitar datos duplicados

    public Account(){}

    public Account(String number, LocalDateTime creationDate, Double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @JsonIgnore //para evitar bucle infinito
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactionSet() {
        return transactions;
    }
    public void addTransaction(Transaction transaction) { //permite conectar a una cuenta con una transacción
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    //método para generar una cuenta:
    public String generateAccountNumber() {
        String accountNumber;
        int numberGenerated = (int) (Math.random() * 1000);
        return accountNumber = "VIN" + String.format("%08d", numberGenerated);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", email='" + balance + '\'' +
                '}';
    }
}
