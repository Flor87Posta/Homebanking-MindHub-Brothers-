package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;


    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(  //método para crear una nueva tarjeta
                                            @RequestParam String typeCard, @RequestParam String color, //el cliente envia estos parametros por formulario
                                            Authentication authentication) { //obtengo un objeto Authentication

        Client client = clientService.findByEmail(authentication.getName()); //comparo aquí con el authentication al cliente autenticado con el jsessionId



        if (typeCard.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }

        if (color.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }

 /*       if (client == null) {
            return new ResponseEntity<>("You aren´t a client", HttpStatus.FORBIDDEN); //código de estado HTTP 403 prohibido
        }*/  //no hace falta porque ya trabajamos con el autenticado

        //variables para el constructor:

        long numberGenerated = (long) (Math.random() * 9000000000000000L) + 1000000000000000L;
            //Math.random() genera un número aleatorio decimal entre 0.0 y 1.0 (no incluyendo 1.0).
            //Multiplicando Math.random() por 9000000000000000L generamos un número aleatorio entre 0 y 89999999999999.
            //Al sumarle 1000000000000000L, obtenemos un número aleatorio entre 100000000000000 y 999999999999999.
            //Al convertir el resultado a un long, tenemos un número entero de 16 dígitos.
        String cardNumber = String.format("%016d", numberGenerated); //paso a String porque asi está en la propiedad
        String cardNumberConGuiones = cardNumber.substring(0, 4) + "-" + cardNumber.substring(4, 8) + "-" +
                    cardNumber.substring(8, 12) + "-" + cardNumber.substring(12, 16);
        int cardCvv = (int) (Math.random() * 900) + 100;
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

        Set<Card> creditCards = client.getCards().stream()
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
    }

    @DeleteMapping("clients/current/delete-card")
    public ResponseEntity<Object> deleteCard(@RequestParam long cardId, Authentication auth) {
        Client client = clientService.findByEmail(auth.getName());
        Optional<Card> optionalCard = cardService.findByIdOptional(cardId);
        if (optionalCard.isPresent()) {
            Card cardToDelete = optionalCard.get();
            if (cardToDelete.getCardholder().equals(client)) {
                cardService.delete(cardToDelete);
                return new ResponseEntity<>("Card deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("This card does not belong to the  client", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        }
    }
    }



