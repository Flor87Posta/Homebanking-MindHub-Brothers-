package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQuery(name="Client.findByEmail", query="select u from Client u where u.email= ?1")
public class Client {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private String password;

    @OneToMany(mappedBy="client", fetch= FetchType.EAGER) //asociado a client, definido en la clase Account
    private Set<Account> accounts = new HashSet<>(); // set para evitar datos duplicados
    //accounts como nuevo atributo de la tabla Client

    @OneToMany(mappedBy="clientLoan", fetch= FetchType.EAGER) //asociado a clientLoan, definido en la clase ClientLoan
    private Set<ClientLoan> clientLoans = new HashSet<>(); // set para evitar datos duplicados

    @OneToMany(mappedBy="client", fetch= FetchType.EAGER) //asociado a client, definido en la clase Card
    private Set<Card> cards = new HashSet<>(); // set para evitar datos duplicados



    public Client(){};

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccountSet() {
        return accounts;
    } //método accesor get de la nueva propiedad creada por la relación con Account

    public Set<ClientLoan> getLoansSet() { //método para retornar el listado de préstamos de un cliente
        return clientLoans;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    // metodos creados add:
    public void addAccount(Account account) { // método público creado que permite conectar a un cliente con una cuenta
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) { // método público creado que permite conectar a un cliente con un préstamo
        clientLoan.setClientLoan(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card) { // método público creado que permite conectar a un cliente con una tarjeta
        card.setClient(this);
        cards.add(card);
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
