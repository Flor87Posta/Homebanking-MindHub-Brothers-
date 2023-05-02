package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication; // este no funciona: import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @RequestMapping(path = "/clients/current/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> makeTransaction
            (@RequestParam String originAccNumber, @RequestParam String destinationAccNumber, //parámetros que recibe desde el front
             @RequestParam double amount, @RequestParam String description,
             Authentication authentication) { // para cliente autenticado:


        Client clientSend = clientRepository.findByEmail(authentication.getName());  //comparo aquí con el authentication al cliente autenticado con el jsessionId
        //si el cliente se transfiere entre sus propias cuentas, es un solo cliente que interviene, pero si es a un tercero:
        Client clientDestin = accountRepository.findByNumber(destinationAccNumber).getClient();

        Account accountOrigin = accountRepository.findByNumber(originAccNumber); // busco en el repo de cuentas a la cuenta de origen que puso el cliente y la guardo en accountOrigin
        Account accountDestin = accountRepository.findByNumber(destinationAccNumber); // aca lo mismo pero para la cuenta destino


        if (originAccNumber.isEmpty()) {
            return new ResponseEntity<>( "Missing origin account", HttpStatus.FORBIDDEN);
        }
        if (destinationAccNumber.isEmpty()) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        if (amount < 1) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        if (description.isEmpty()) {
            return new ResponseEntity<>("No description provided", HttpStatus.FORBIDDEN);
        }
        if (originAccNumber.equals(destinationAccNumber)) {
            return new ResponseEntity<>("It is not possible to send money to the same account", HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsByNumber(originAccNumber)) {
            return new ResponseEntity<>( "Origin Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(destinationAccNumber)) {
            return new ResponseEntity<>("Destination Account doesn't exist",  HttpStatus.FORBIDDEN);
        }
        //Verificar que la cuenta de origen pertenezca al cliente autenticado:

        if (!clientSend.getAccountSet().contains(accountOrigin)) {
            return new ResponseEntity<>("You don't have this account", HttpStatus.FORBIDDEN);
        }
        //Verificar que la cuenta de origen tenga el monto disponible:

        if (accountOrigin.getBalance() < amount) {
            return new ResponseEntity<>("You don't have this amount", HttpStatus.FORBIDDEN);
        }

        if (description.length() > 20) {
            return new ResponseEntity<>("Description exceeds maximum length of 20 characters", HttpStatus.FORBIDDEN);
        }

        String newDescriptionAccountOrigin = accountOrigin.getNumber() + description;
        Transaction debit = new Transaction(TransactionType.DEBIT, -amount, newDescriptionAccountOrigin, LocalDateTime.now() );
        transactionRepository.save(debit);
        //accountOrigin.getBalance()-amount; // ¿como actualizo el saldo?
        accountOrigin.addTransaction(debit);
        accountRepository.save(accountOrigin);
        //clientDestin.addAccount(); ya queda vinculado al cliente?

        String newDescriptionAccountDestin = accountDestin.getNumber() + description;
        Transaction credit = new Transaction(TransactionType.CREDIT, amount, newDescriptionAccountDestin, LocalDateTime.now() );
        transactionRepository.save(credit);
        //accountOrigin.getBalance()-amount; // ¿como actualizo el saldo?
        accountDestin.addTransaction(credit);
        accountRepository.save(accountDestin);

        return new ResponseEntity<>(" Successfull Transaction ", HttpStatus.CREATED); //código de estado HTTP 201 creado
    }
}



//Se deben crear dos transacciones, una con el tipo de transacción “DEBIT” asociada a la cuenta de origen
// y la otra con el tipo de transacción “CREDIT” asociada a la cuenta de destino:

//A la cuenta de origen se le restará el monto indicado en la petición y a la cuenta de destino se le sumará el mismo monto:

//guardarlas en el repositorio de transacciones

//Una vez realizada la creación de las transacciones, debes actualizar cada cuenta con los montos correspondientes ¿como?
// y guardarlas a través del repositorio de cuentas.
