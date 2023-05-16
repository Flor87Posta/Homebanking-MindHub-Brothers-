package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

//    @RequestMapping("/accounts")
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() { //definí un método público llamado getAccounts del tipo Lista que retorna una List d Accounts
        return accountService.getAccounts();
    }

//    @RequestMapping("/accounts/{id}")
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) { //definí un método público llamado getAccount que retorna una cuenta por su id
        return accountService.getAccount(id);
    }

//    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount( //método para crear una nueva cuenta
            Authentication authentication, @RequestParam String accountType) { //obtengo un objeto Authentication; antes: no requería parametros desde el front

        Client client = clientService.findByEmail(authentication.getName()); //comparo aquí con el authentication al cliente autenticado con el jsessionId
        List<Account> accounts = client.getAccountSet().stream().collect(toList());

        if ( !accountType.equalsIgnoreCase("SAVINGS") && !accountType.equalsIgnoreCase("CURRENT")){
            return new ResponseEntity<>("Please select an account type .", HttpStatus.FORBIDDEN);}

        if (accounts.size() < 3) {
            try { //bloque de código a intentar:
                String accountNumber;
                do {
                    accountNumber = AccountUtils.getAccountNumber();
                } while (accountService.findByNumber(accountNumber) != null); //para corroborar que esa cuenta no exista ya
                Account accountGenerated = new Account(accountNumber, LocalDateTime.now(), 0.0, AccountType.valueOf(accountType.toUpperCase()));
                accountService.saveNewAccount(accountGenerated);
                client.addAccount(accountGenerated);
                clientService.saveNewClient(client);
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            } catch (IllegalArgumentException ex) { //especifica una respuesta si se produce una excepción
                //IllegalArgumentException es una excepción de Java que indica que un método ha recibido un argumento que no es válido o es inapropiado para los fines de este método.
                //Esta excepción se utiliza normalmente cuando el procesamiento posterior en el método depende del argumento no válido y no puede continuar a menos que se proporcione un argumento adecuado en su lugar.
                return new ResponseEntity<>("Invalid account type", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Client already has 3 or more accounts", HttpStatus.FORBIDDEN);
        }
    }


}
