package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;


//    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(  //método para crear una nueva tarjeta
                                            @RequestParam String typeCard, @RequestParam String color, //el cliente envia estos parametros por formulario
                                            Authentication authentication) { //obtengo un objeto Authentication

        Client client = clientService.findByEmail(authentication.getName()); //comparo aquí con el authentication al cliente autenticado con el jsessionId


        if (typeCard.isEmpty()) {
            return new ResponseEntity<>("Please insert a type Card", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }

        if (color.isEmpty()) {
            return new ResponseEntity<>("Please insert a color Card", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }

 /*       if (client == null) {
            return new ResponseEntity<>("You aren´t a client", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }*/  //no hace falta porque ya trabajamos con el autenticado

        //variables para el constructor:

        //método getCardNumberConGuiones()
        //Math.random() genera un número aleatorio decimal entre 0.0 y 1.0 (no incluyendo 1.0).
        //Multiplicando Math.random() por 9000000000000000L generamos un número aleatorio entre 0 y 89999999999999.
        //Al sumarle 1000000000000000L, obtenemos un número aleatorio entre 100000000000000 y 999999999999999.
        //Al convertir el resultado a un long, tenemos un número entero de 16 dígitos. dp: //paso a String porque asi
        // está en la propiedad

        String cardNumberConGuiones = CardUtils.getCardNumberConGuiones();

        int cardCvv = CardUtils.getCardCvv();
        //Math.random() genera un número aleatorio decimal entre 0.0 y 1.0 (no incluyendo 1.0).
        //Multiplicando Math.random() por 900 generamos un número aleatorio entre 0 y 899.
        //Al sumarle 100, obtenemos un número aleatorio entre 100 y 999.
        //Al convertir el resultado a un int, tenemos un número entero de 3 dígitos.
        //Ten en cuenta que el resultado de Math.random() es un número decimal, por lo que al multiplicarlo por 900,
        //obtendrás un número decimal entre 0.0 y 899.9... Para convertir este número decimal en un entero, se utiliza la operación de truncamiento al convertirlo a int.
        CardType enumType = CardType.valueOf(typeCard);
        CardColor enumColor = CardColor.valueOf(color);

       /* if(cardRepository.existsByCardDigits(cardNumber)){ // para verificar que no exista ese numero de tarjetas
            return new ResponseEntity<>("Max owned cards reached", HttpStatus.FORBIDDEN);
        }*/ //NO FUNCIONA, SEGUIR PROBANDO

               /*       int creditCards = 0;
                int debitCards = 0;
                for (Card card : client.getCards()) {
                    if (card.getTypeCard() == CardType.CREDIT) {
                        creditCards++;
                    } else if (card.getTypeCard() == CardType.DEBIT) {
                        debitCards++;
                    }
                }*/ //no funciona porque tiene que ser SET para luego poder aplicar size();

        //filtrado de cards, por tipo y color, y dentro de color por tipo de nuevo:

  /*      Set<Card> creditCards = client.getCards().stream()
                .filter(card -> card.getTypeCard() == CardType.CREDIT)
                .collect(Collectors.toSet());

        Set<Card> debitCards = client.getCards().stream()
                .filter(card -> card.getTypeCard() == CardType.DEBIT)
                .collect(Collectors.toSet());

        Set<Card> goldCards = client.getCards().stream()
                .filter(card -> card.getColor() == CardColor.GOLD)
                .collect(Collectors.toSet());

        Set<Card> goldCardsDebit = goldCards.stream()
                .filter(card -> card.getTypeCard() == CardType.DEBIT)
                .collect(Collectors.toSet());

        Set<Card> goldCardsCredit = goldCards.stream()
                .filter(card -> card.getTypeCard() == CardType.CREDIT)
                .collect(Collectors.toSet());

        Set<Card> silverCards = client.getCards().stream()
                .filter(card -> card.getColor() == CardColor.SILVER)
                .collect(Collectors.toSet());

        Set<Card> silverCardsDebit = silverCards.stream()
                .filter(card -> card.getTypeCard() == CardType.DEBIT)
                .collect(Collectors.toSet());

        Set<Card> silverCardsCredit = silverCards.stream()
                .filter(card -> card.getTypeCard() == CardType.CREDIT)
                .collect(Collectors.toSet());

        Set<Card> titaniumCards = client.getCards().stream()
                .filter(card -> card.getColor() == CardColor.TITANIUM)
                .collect(Collectors.toSet());

        Set<Card> titaniumCardsDebit = titaniumCards.stream()
                .filter(card -> card.getTypeCard() == CardType.DEBIT)
                .collect(Collectors.toSet());

        Set<Card> titaniumCardsCredit = titaniumCards.stream()
                .filter(card -> card.getTypeCard() == CardType.CREDIT)
                .collect(Collectors.toSet());


        if (creditCards.size() < 3) {
            if (goldCardsCredit.size() < 1) {
                Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), enumType, enumColor, cardNumberConGuiones, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
                cardService.saveNewCard(cardGenerated);
                client.addCard(cardGenerated);
                clientService.saveNewClient(client);
                return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
            } else if (silverCardsCredit.size() < 1) {
                Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), enumType, enumColor, cardNumberConGuiones, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
                cardService.saveNewCard(cardGenerated);
                client.addCard(cardGenerated);
                clientService.saveNewClient(client);
                return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
            } else if (titaniumCardsCredit.size() < 1) {
                Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), enumType, enumColor, cardNumberConGuiones, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
                cardService.saveNewCard(cardGenerated);
                client.addCard(cardGenerated);
                clientService.saveNewClient(client);
                return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Client already has 3 or more creditCards", HttpStatus.FORBIDDEN);
            }
        }


        if (debitCards.size() < 3) {
            if (goldCardsDebit.size() < 1) {
                Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), enumType, enumColor, cardNumberConGuiones, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
                cardService.saveNewCard(cardGenerated);
                client.addCard(cardGenerated);
                clientService.saveNewClient(client);
                return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
            } else if (silverCardsDebit.size() < 1) {
                Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), enumType, enumColor, cardNumberConGuiones, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
                cardService.saveNewCard(cardGenerated);
                client.addCard(cardGenerated);
                clientService.saveNewClient(client);
                return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
            } else if (titaniumCardsDebit.size() < 1) {
                Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), enumType, enumColor, cardNumberConGuiones, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
                cardService.saveNewCard(cardGenerated);
                client.addCard(cardGenerated);
                clientService.saveNewClient(client);
                return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Client already has 3 or more debitCards", HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>("Client already has 3 or more debitCards and creditCards", HttpStatus.FORBIDDEN);
    }*/

        Set<Card> cards = client.getCards().stream().filter(card -> card.getTypeCard().equals(CardType.valueOf(typeCard.toUpperCase())) && card.getColor().equals(CardColor.valueOf(color.toUpperCase()))).collect(toSet());
        if (cards.size() > 0) {
            return new ResponseEntity<>("You already have a " + typeCard.toLowerCase() + " " + color.toLowerCase() + " card", HttpStatus.FORBIDDEN);
        }
        Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), enumType, enumColor, cardNumberConGuiones, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5), false);
        cardService.saveNewCard(cardGenerated);
        client.addCard(cardGenerated);
        clientService.saveNewClient(client);
        return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
    }

    @PostMapping("clients/current/delete-card") //aunque no borramos, solo escondemos
    public ResponseEntity<Object> deleteCard(@RequestParam String requestedNumber, Authentication auth) {
        Client client = clientService.findByEmail(auth.getName());
        Card numberCard = cardService.findByNumber(requestedNumber); //el num de tarjeta que ingresa el cliente lo busco
        //en el repo y lo guardo en numberCard,
        if( numberCard == null ){
            return new ResponseEntity<>("this card doesn't exist", HttpStatus.FORBIDDEN);}
        if (numberCard.getClient()==client) {
                numberCard.setHidden(true);
                cardService.saveNewCard(numberCard);
                return new ResponseEntity<>("Card deleted successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("This card is not a client´s card", HttpStatus.FORBIDDEN);
            }
    }

    @Transactional
    @PostMapping("clients/current/pay-card")
    public ResponseEntity<Object> payWithCard(@RequestBody PaymentDTO paymentDTO)  {
        Client client = clientService.findByEmail(paymentDTO.getEmail());

        if (!EnumSet.of(CardType.CREDIT, CardType.DEBIT).contains(paymentDTO.getTypeCard())) {
            return new ResponseEntity<>("Invalid card type", HttpStatus.FORBIDDEN);
        }

        if(!cardService.existsByNumber(paymentDTO.getNumber())){
            return new ResponseEntity<>("Invalid card", HttpStatus.FORBIDDEN);
        }

        Card cardUsed = cardService.findByNumber(paymentDTO.getNumber());

        boolean hasCard = client.getCards()
                .stream()
                .anyMatch(card -> card.getId() == cardUsed.getId());

        if(!hasCard){
            return new ResponseEntity<>("You don't have this card", HttpStatus.FORBIDDEN);
        }

        if(cardUsed.getThruDate().isBefore(LocalDate.of(2023, 5, 16))){
            return new ResponseEntity<>("This card is expired", HttpStatus.FORBIDDEN);
        }

       Optional<Account> optionalAccountToBeDebited = client.getAccountSet()
                .stream()
                .filter(acc -> acc.getBalance() >= paymentDTO.getAmount())
                .findFirst();

        if (optionalAccountToBeDebited.isPresent()) {
            Account accountToBeDebited = optionalAccountToBeDebited.get();
            // Realizo las operaciones con la cuenta encontrada


            Double initialBalanceaccountToBeDebited = accountToBeDebited.getBalance() - paymentDTO.getAmount();
            Transaction paymentCard = new Transaction(TransactionType.DEBIT, paymentDTO.getAmount(), paymentDTO.getDescription(), LocalDateTime.now(),initialBalanceaccountToBeDebited );
            transactionService.saveNewTransaction(paymentCard);
            accountToBeDebited.addTransaction(paymentCard);
            double newBalanceDebit = accountToBeDebited.getBalance() - paymentDTO.getAmount(); // Calcula el nuevo saldo
            accountToBeDebited.setBalance(newBalanceDebit); // Actualizo el saldo
            accountService.saveNewAccount(accountToBeDebited); //guardo la cuenta

            return new ResponseEntity<>("Payment successful", HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>("Insufficient Funds", HttpStatus.FORBIDDEN);

        }

    }
        }




