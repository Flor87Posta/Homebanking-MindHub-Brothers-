package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy="client", fetch= FetchType.EAGER) //asociado a client, definido en la clase Account
    private Set<Account> accounts = new HashSet<>(); // set para evitar datos duplicados
    //accounts como nuevo atributo de la tabla Client


    public Client(){};
    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccountSet() {
        return accounts;
    }
    public void addAccount(Account account) { //permite conectar a un cliente con una cuenta
        account.setClient(this);
        accounts.add(account);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
