package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients") // si no se aclara método por defecto es GET
    public List<ClientDTO> getClients() { //definí un método público llamado getClients del tipo Lista que retorna una List<ClientDTO>
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) { //definí un método público llamado getClient que retorna un clientDTO por su id
        Optional<Client> optionalClient = clientRepository.findById(id);
        return optionalClient.map(client -> new ClientDTO(client)).orElse(null);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST) //método para registrar un nuevo cliente
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) { //son todos los parámetros que envía el usuario al registrarse


        if (firstName.isEmpty() ) {

            return new ResponseEntity<>("Please, write your firstname", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }


        if (lastName.isEmpty()) {

            return new ResponseEntity<>("Please, write your lastname", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }

        if (email.isEmpty()) {

            return new ResponseEntity<>("Please, write your email", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }

        if (password.isEmpty()) {

            return new ResponseEntity<>("Please, write your password", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }
        
        if (clientRepository.findByEmail(email) != null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido

        }
        String accountNumber;
        do {
            int numberGenerated = (int) (Math.random() * 1000);
            accountNumber = "VIN" + String.format("%08d", numberGenerated);
        } while (accountRepository.findByNumber(accountNumber) != null); //findByNumber: método creado en account repository

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0.0);
        clientRepository.save(newClient);
        newClient.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED); //código de estado HTTP 201 creado

    }
    @RequestMapping("/clients/current") // este servlet es para crear un nuevo servicio que retorne toda la información del usuario autenticado, antes usabamos /clients/1 que es Melba
        public ClientDTO getClientCurrent(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());//Si hay un usuario conectado, authentication.getName() devolverá el nombre que la clase WebAuthentication puso en el objeto User.
        Set<Account> clientAccountSet =  client.getAccountSet(); //vinculo las cuentas al cliente
        client.setAccounts(clientAccountSet);
        Set<Card> clientCardSet = client.getCards(); //vinculo las tarjetas al cliente
        client.setCards(clientCardSet);
        return new ClientDTO(client); //al client que cree que guarda el usuario autenticado lo transformo en clientDTO que tiene todas las propiedades
    }
}



