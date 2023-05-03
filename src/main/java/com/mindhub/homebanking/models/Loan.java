package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    public static String name;
    private static double maxAmount ;
    @ElementCollection
    @Column(name="payments")
    private static List<Integer> payments = new ArrayList<>();

    //Relaciones:

    @OneToMany(mappedBy="loan", fetch= FetchType.EAGER) //asociado a loan, definido en la clase ClientLoan
    private Set<ClientLoan> clientLoans = new HashSet<>(); // set para evitar datos duplicados
    // loan_id como nuevo atributo de la tabla ClientLoan

    //constructores:

    public Loan (){}
    public Loan (String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    //métodos accesores:


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public static List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<ClientLoan> getClientLoanSet() { //método creado para retornar el listado de clientes de un préstamo
        return clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan) { // método público creado que permite conectar a un prestamo con un clientloan
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                ", payments=" + payments +
                '}';
    }
}
