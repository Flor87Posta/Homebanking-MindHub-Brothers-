package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static java.util.stream.Collectors.toList;

@Transactional
@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

//    @RequestMapping(path = "/loans",method = RequestMethod.GET)
    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();} //retorna una lista de loanDTO


//    @RequestMapping(path = "/clients/current/loans", method = RequestMethod.POST)
    @PostMapping("/clients/current/loans")
    public ResponseEntity<Object> requestLoan
            (@RequestBody LoanApplicationDTO loanApplicationDTO, //parámetro en forma de objeto que recibe desde el front
             Authentication authentication) { // para cliente autenticado:

        Client client = clientService.findByEmail(authentication.getName());  //comparo aquí con el authentication al cliente autenticado con el jsessionId

        Account clientAcc = accountService.findByNumber(loanApplicationDTO.getDestinationAccNumber()); //traigo la cuenta destino del objeto que ingreso como parametro

        Loan loan = loanService.findById(loanApplicationDTO.getLoanId());//traigo el tipo de préstamo elegido

        double addedAmount = 0;

        if (loanApplicationDTO.getDestinationAccNumber().isEmpty()) { //verifico que no este vacio el campo de cuenta destino
            return new ResponseEntity<>("Invalid account", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getLoanId() == 0) { //verifico que no este vacío el campo de tipo de prestamo elegido
            return new ResponseEntity<>("Invalid loan type", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() <= 0) { //verifico que el monto no sea cero o negativo
            return new ResponseEntity<>("Amount  must be positive", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getPayments() <= 0) { //verifico que las cuotas no sean cero o negativo
            return new ResponseEntity<>("Payments  must be positive", HttpStatus.FORBIDDEN);
        }
        if (loan == null) {
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsByNumber(loanApplicationDTO.getDestinationAccNumber())) {  //verifico que la cuenta exista:
            return new ResponseEntity<>("Destination Account doesn't exist", HttpStatus.FORBIDDEN);
        }

        //para que el monto solicitado no exceda el max monto otorgable:

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) { //me habia pedido hacer static este metodo de getMaxAmount en Loan.. que signifca?
            return new ResponseEntity<>("Amount exceed max amount available", HttpStatus.FORBIDDEN);
        }


        // La solicitud de hacer este método getPayments() estático en la clase Loan significa que el método no depende del
        // estado actual de ningún objeto Loan. En otras palabras, no necesita ser llamado en una instancia de objeto Loan,
        // sino que puede ser llamado directamente en la clase Loan sin la necesidad de crear una instancia primero.
        //Al hacer que el método sea estático, se puede llamar directamente desde cualquier parte del código sin tener que
        // crear una instancia de Loan. En este caso específico, el método getPayments() se utiliza para comprobar si una
        // lista de pagos ya está incluida en una lista de pagos existente en un objeto Loan. Al hacer que el método sea estático,
        // se puede llamar directamente en la clase Loan, sin necesidad de crear una instancia de Loan en primer lugar,
        // lo que podría ser útil en algunos casos.


        //para que las cuotas solicitadas no excedan el max d cuotas otorgables:

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Payments exceed max payments available", HttpStatus.FORBIDDEN);
        }

        //para verificar que la cuenta destino pertenezca al cliente autenticado
        if (client.getAccountSet().stream().noneMatch(account -> account.getId() == clientAcc.getId())) {
            return new ResponseEntity<>("You don't have this account", HttpStatus.FORBIDDEN);
        }

        //para verificar que no posea otro préstamo del mismo tipo:

        List<String> loans = client.getClientLoans().stream().map(clientLoan -> clientLoan.getLoan().getName()).collect(Collectors.toList());
        if (loans.contains(loan.getName())){
            return new ResponseEntity<>("You've already taken out a loan of this category", HttpStatus.FORBIDDEN);
        }
      /*  if (client.getClientLoans().stream().anyMatch(loanc -> loan.getId()==(loanApplicationDTO.getLoanId()))) {
            return new ResponseEntity<>("You've already taken out a loan of this category", HttpStatus.FORBIDDEN);
        }*/ // if (client.getLoans().stream().anyMatch(loanc -> loan.getId().equals(loanApplicationDTO.getLoanId())))

        if(loanApplicationDTO.getLoanId()==1){
            addedAmount = loanApplicationDTO.getAmount()* 1.20;
        }
        if(loanApplicationDTO.getLoanId()==2){
            addedAmount = loanApplicationDTO.getAmount()* 1.40;
        }
        if(loanApplicationDTO.getLoanId()==3){
            addedAmount = loanApplicationDTO.getAmount()* 1.50;
        }
        //entonces ahora para crear el préstamo creo primero la transacción del tipo CREDIT:
        String newDescriptionLoan = loan.getName() + "loan approved";

        Transaction creditLoan = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), newDescriptionLoan, LocalDateTime.now());
        clientAcc.addTransaction(creditLoan); //Agrego la transacción a la cuenta destino
        transactionService.saveNewTransaction(creditLoan);
        double newBalanceCredit = clientAcc.getBalance() + loanApplicationDTO.getAmount(); // Calcula el nuevo saldo
        clientAcc.setBalance(newBalanceCredit); // Actualizo el saldo
        accountService.saveNewAccount(clientAcc); //guardo la cuenta

        // pero falta crear el préstamo en sí, con la cantidad de cuotas, el monto y el nombre del préstamo, es un clientloan? o creo entidad nueva?
        //podria usar un new ClientLoan... porque Loan no me deja porq tiene la lista de cuotas...
        //y falta generar con el método de abajo para el plan de cuotas...

        ClientLoan clientLoan = new ClientLoan(addedAmount, loanApplicationDTO.getPayments());
        //falta vincularle el client y el loan;porque yo no los puse en el constructor de ClientLoan a esos parametros:
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientLoanRepository.save(clientLoan);
        loanService.saveNewLoan(loan);
        clientService.saveNewClient(client);

        return new ResponseEntity<>(" Successfull Transaction ", HttpStatus.CREATED); //código de estado HTTP 201 creado
    }



    //método para calcular las cuotas del préstamo:
  /*  private double calculatePayments(double loanAmount, double interestRate) {
        return loanAmount * (1 + (interestRate / 100));
    }*/



}
