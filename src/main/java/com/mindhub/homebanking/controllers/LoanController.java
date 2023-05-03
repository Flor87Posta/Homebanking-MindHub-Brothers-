package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LoanRepository loanRepository;


    @RequestMapping(path = "/clients/current/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> requestLoan
            (@RequestBody LoanApplicationDTO loanApplicationDTO, //parámetro en forma de objeto que recibe desde el front
             Authentication authentication) { // para cliente autenticado:

        Client client = clientRepository.findByEmail(authentication.getName());  //comparo aquí con el authentication al cliente autenticado con el jsessionId

        Account clientAcc = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccNumber()); //traigo la cuenta destino del objeto que ingreso como parametro

        Optional<Loan> loanType = loanRepository.findById(loanApplicationDTO.getLoanId()); //traigo el tipo de prestamo elegido

/*        if (client == null){
            return new ResponseEntity<>("You aren´t a client", HttpStatus.UNAUTHORIZED); //código de estado HTTP 401 no autenticado
        }*/ //no hace falta porque el authentication ya corrobora

        if (loanApplicationDTO.getDestinationAccNumber().isEmpty()) { //verifico que no este vacio el campo de cuenta destino
            return new ResponseEntity<>("Invalid account", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getLoanId().isEmpty()) { //verifico que no este vacio el campo de tipo de prestamo elegido
            return new ResponseEntity<>("Invalid loan type", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() <= 0) { //verifico que el monto no sea cero o negativo
            return new ResponseEntity<>("Amount  must be positive", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getPayments() <= 0) { //verifico que las cuotas no sean cero o negativo
            return new ResponseEntity<>("Payments  must be positive", HttpStatus.FORBIDDEN);
        }


        if (!accountRepository.existsByNumber(loanApplicationDTO.getDestinationAccNumber())) {  //verifico que la cuenta exista:
            return new ResponseEntity<>("Destination Account doesn't exist", HttpStatus.FORBIDDEN);
        }

        //para que el monto solicitado no exceda el max monto otorgable:
        if (loanApplicationDTO.getAmount() > Loan.getMaxAmount()) { //me pidio hacer static este metodo de getMaxAmount en Loan.. que signifca?
            return new ResponseEntity<>("Amount exceed max amount available", HttpStatus.FORBIDDEN);
        }

        //para que las cuotas solicitadas no excedan el max d cuotas otorgables:

        if (!Loan.getPayments().contains(loanApplicationDTO.getPayments())) { //me pidio hacer static este metodo de getPayments en Loan.. que signifca?
            return new ResponseEntity<>("Payments exceed max payments available", HttpStatus.FORBIDDEN);
        }

        //para verificar que la cuenta destino pertenezca al cliente autenticado
        if (client.getAccountSet().stream().noneMatch(account -> account.getId() == clientAcc.getId())) {
            return new ResponseEntity<>("You don't have this account", HttpStatus.FORBIDDEN);
        }

        //entonces ahora para crear el préstamo creo primero la transacción del tipo CREDIT:
        String newDescriptionLoan = Loan.getName() + "loan approved"; //pide tambien hace estatico el name

        Transaction creditLoan = new Transaction (TransactionType.CREDIT, loanApplicationDTO.getAmount(), newDescriptionLoan, LocalDateTime.now());
        transactionRepository.save(creditLoan);
        clientAcc.addTransaction(creditLoan); //Agrego la transacción a la cuenta destino
        double newBalanceCredit = clientAcc.getBalance() + loanApplicationDTO.getAmount(); // Calcula el nuevo saldo
        clientAcc.setBalance(newBalanceCredit); // Actualizo el saldo
        accountRepository.save(clientAcc); //guardo la cuenta

        // pero falta crear el préstamo en sí, con la cantidad de cuotas, el monto y el nombre del préstamo, es un loan? o creo entidad nueva?
        //podria usar un new ClientLoan? porque Loan no me deja porq tiene la lista de cuotas...
        //y falta generar con el método de abajo el plan de cuotas...




        return new ResponseEntity<>(" Successfull Transaction ", HttpStatus.CREATED); //código de estado HTTP 201 creado
    }


    //método para calcular las cuotas del préstamo:
    private double calculatePayments(double loanAmount, double interestRate) {
        return loanAmount * (1 + (interestRate / 100));
    }



}
