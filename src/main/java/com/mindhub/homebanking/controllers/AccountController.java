package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;

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
/*    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) { //definí un método público llamado getAccount que retorna una cuenta por su id
        return accountService.getAccount(id);
    }*/
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication auth) {
    // Obtenemos el cliente autenticado
    Client client = clientService.findByEmail(auth.getName());

    // Buscamos la cuenta por su ID
    Account account = accountService.findById(id);

    // Verificamos si el cliente es propietario de la cuenta
    if (account != null && account.getClient() == client) {
      AccountDTO accountDTO = new AccountDTO(account);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }
    // El cliente no es propietario de la cuenta o la cuenta no existe
    return new ResponseEntity<>("You do not own this account or the account does not exist.", HttpStatus.FORBIDDEN);
}


//    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount( //método para crear una nueva cuenta
            Authentication authentication, @RequestParam String accountType) { //obtengo un objeto Authentication; antes: no requería parametros desde el front

        Client client = clientService.findByEmail(authentication.getName()); //comparo aquí con el authentication al cliente autenticado con el jsessionId
//        List<Account> accounts = client.getAccountSet().stream().collect(toList());
        List<Account> accounts = accountService.findByClient(client);

        if ( !accountType.equalsIgnoreCase("SAVINGS") && !accountType.equalsIgnoreCase("CURRENT")){
            return new ResponseEntity<>("Please select an account type .", HttpStatus.FORBIDDEN);}

        if (accounts.size() < 3) {
            try { //bloque de código a intentar:
                String accountNumber;
                do {
                    accountNumber = AccountUtils.getAccountNumber();
                } while (accountService.findByNumber(accountNumber) != null); //para corroborar que esa cuenta no exista ya
                Account accountGenerated = new Account(accountNumber, LocalDateTime.now(), 0.0, AccountType.valueOf(accountType.toUpperCase()), false);
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


    @PostMapping("clients/current/delete-account")
    public ResponseEntity<Object> deleteAccount(@RequestParam long accId, Authentication auth) { //para ocultarla
        Client client = clientService.findByEmail(auth.getName());
        Account account = accountService.findById(accId);
        List<Account> accounts = accountService.findByClient(client);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with id " + account.getNumber() + " not found.");
        }
        if (account.getClient() == client && accounts.contains(account)) { //utilizando findByClient (cuentas activas del cliente)

            if (accounts.size() == 1 && accounts.get(0).equals(account)) {  // verificar si la cuenta que se está eliminando es la última que tiene el cliente:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete your only account.");
            }else if(account.getBalance()>0){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You still have balance in this account.");
            }else {
                account.setHidden(true);
                accountService.saveNewAccount(account);
                return ResponseEntity.ok("Account deleted successfully");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this account with.");
        }
    }
    }