package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

    public class Transaction {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private long id;
        private TransactionType type;
        private Double amount;
        private String description;
        private LocalDateTime transactionDate;
        private double balanceTransaction;




        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name="account_id")
        private Account account;

        public Transaction() {}

        public Transaction(TransactionType type, Double amount, String description, LocalDateTime transactionDate, double balanceTransaction) {
            this.type = type;
            this.amount = amount;
            this.description = description;
            this.transactionDate = transactionDate;
            this.balanceTransaction = balanceTransaction;

        }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getBalanceTransaction() {
        return balanceTransaction;
    }

    public void setBalanceTransaction(double balanceTransaction) {
        this.balanceTransaction = balanceTransaction;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }



    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", transactionDate=" + transactionDate +
                ", balanceTransaction=" + balanceTransaction +
                ", account=" + account +
                '}';
    }
}


