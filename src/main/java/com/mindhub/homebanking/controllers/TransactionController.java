package com.mindhub.homebanking.controllers;
import com.itextpdf.text.DocumentException;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication; // este no funciona: import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static java.time.LocalDateTime.parse;

@Transactional
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;




//    @RequestMapping(path = "/clients/current/transactions", method = RequestMethod.POST)
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> makeTransaction
            (@RequestParam String originAccNumber, @RequestParam String destinationAccNumber, //parámetros que recibe desde el front
             @RequestParam double amount, @RequestParam String description,
             Authentication authentication) { // para cliente autenticado:


        Client clientSend = clientService.findByEmail(authentication.getName());  //comparo aquí con el authentication al cliente autenticado con el jsessionId
        //si el cliente se transfiere entre sus propias cuentas, es un solo cliente que interviene, pero si es a un tercero busco la cuenta y obtengo el cliente:
        Client clientDestin = accountService.findByNumber(destinationAccNumber).getClient();

        Account accountOrigin = accountService.findByNumber(originAccNumber); // busco en el repo de cuentas a la cuenta de origen que puso el cliente y la guardo en accountOrigin
        Account accountDestin = accountService.findByNumber(destinationAccNumber); // aca lo mismo pero para la cuenta destino


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

        if (!accountService.existsByNumber(originAccNumber)) {
            return new ResponseEntity<>( "Origin Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsByNumber(destinationAccNumber)) {
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

        Double initialOriginBalanceAccount = accountOrigin.getBalance() - amount;
        String newDescriptionAccountOrigin = accountOrigin.getNumber() + description;
        Transaction debit = new Transaction(TransactionType.DEBIT, -amount, newDescriptionAccountOrigin, LocalDateTime.now(),initialOriginBalanceAccount );
        transactionService.saveNewTransaction(debit);
        accountOrigin.addTransaction(debit); //Agrego la transacción a la cuenta
        double newBalanceDebit = accountOrigin.getBalance() - amount; // Calcula el nuevo saldo
        accountOrigin.setBalance(newBalanceDebit); // Actualizo el saldo
        accountService.saveNewAccount(accountOrigin); //guardo la cuenta
        //clientDestin.addAccount(); ya queda vinculado al cliente? si, porque la cuenta esta vinculada al cliente ya por la relacion

        String newDescriptionAccountDestin = accountDestin.getNumber() + description;
        Double initialDestinBalanceAccount = accountDestin.getBalance() + amount;
        Transaction credit = new Transaction(TransactionType.CREDIT, amount, newDescriptionAccountDestin, LocalDateTime.now(),initialDestinBalanceAccount );
        transactionService.saveNewTransaction(credit);
        accountDestin.addTransaction(credit); //Agrego la transacción a la cuenta
        double newBalanceCredit = accountDestin.getBalance() + amount; // Calcula el nuevo saldo
        accountDestin.setBalance(newBalanceCredit); // Actualizo el saldo
        accountService.saveNewAccount(accountDestin); //guardo la cuenta

        return new ResponseEntity<>(" Successfull Transaction ", HttpStatus.CREATED); //código de estado HTTP 201 creado
    }

    @PostMapping ("/clients/current/export-pdf")
    public ResponseEntity<Object> ExportingToPDF(HttpServletResponse response, Authentication authentication, @RequestParam String accNumber, @RequestParam String dateIni, @RequestParam String dateEnd) throws DocumentException, IOException {

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(accNumber);

        if(client == null){
            return new ResponseEntity<>("You are not a Client", HttpStatus.FORBIDDEN);
        }

        if (account == null) {
            return new ResponseEntity<>("Invalid account number", HttpStatus.FORBIDDEN);
        }

        if(client.getAccountSet()
                .stream()
                .noneMatch(account1 -> account1.getNumber().equals(account.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);}

        if (dateIni.isBlank()){
            return new ResponseEntity<>("Start date can't on blank", HttpStatus.FORBIDDEN);
        }else if(dateEnd.isBlank()){
            return new ResponseEntity<>("End date can't be on blank", HttpStatus.FORBIDDEN);
        }

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Transactions" + accNumber + ".pdf";
        response.setHeader(headerKey, headerValue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeIni = LocalDateTime.parse(dateIni, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(dateEnd, formatter);

        List<Transaction> listTransactions = transactionService.findByCreatedBetweenDates(client, accNumber, dateTimeIni, dateTimeEnd);

        TransactionPDFExporter exporter = new TransactionPDFExporter(listTransactions);
        exporter.usePDFExport(response); // Genera el archivo PDF y envíalo como respuesta

        return new ResponseEntity<>("PDF is created", HttpStatus.CREATED);
      /*  response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Transactions" + accNumber + ".pdf";
        response.setHeader(headerKey, headerValue);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeIni = parse(dateIni, formatter);
        LocalDateTime dateTimeEnd= parse(dateEnd,formatter);

        System.out.println(dateTimeEnd + "and" + dateTimeIni);

        List<Transaction> listTransactions =transactionService.findByCreatedBetweenDates(client,accNumber,dateTimeIni,dateTimeEnd) ;
        System.out.println(response);
        TransactionPDFExporter exporter = new TransactionPDFExporter(listTransactions);
        exporter.usePDFExport(response);
        System.out.println("PDF created");

        return new ResponseEntity<>("PDF is created", HttpStatus.CREATED);*/
    }
}



