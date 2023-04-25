package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;


    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(  //método para crear una nueva tarjeta
                                            @RequestParam String typeCard, @RequestParam String color, //el cliente envia estos parametros por formulario
                                            Authentication authentication) { //obtengo un objeto Authentication

        Client client = clientRepository.findByEmail(authentication.getName()); //comparo aquí con el authentication al cliente autenticado con el jsessionId
        if (client.getCards().size() < 6) {
            try { //bloque de código a intentar:

       /*         int creditCards = 0;
                int debitCards = 0;
                for (Card card : client.getCards()) {
                    if (card.getTypeCard() == CardType.CREDIT) {
                        creditCards++;
                    } else if (card.getTypeCard() == CardType.DEBIT) {
                        debitCards++;
                    }
                }*/ //no funciona porque tiene que ser SET para luego poder aplicar size();

                Set<Card> creditCards = client.getCards().stream()
                        .filter(card -> card.getTypeCard() == CardType.CREDIT)
                        .collect(Collectors.toSet());

                Set<Card> debitCards = client.getCards().stream()
                        .filter(card -> card.getTypeCard() == CardType.DEBIT)
                        .collect(Collectors.toSet());

                if (creditCards.size() < 3 && debitCards.size() < 3) {


                    long numberGenerated = (long) (Math.random() * 900000000000L) + 100000000000L;
                    //Math.random() genera un número aleatorio decimal entre 0.0 y 1.0 (no incluyendo 1.0).
                    //Multiplicando Math.random() por 900000000000L generamos un número aleatorio entre 0 y 899999999999.
                    //Al sumarle 100000000000L, obtenemos un número aleatorio entre 100000000000 y 999999999999.
                    //Al convertir el resultado a un long, tenemos un número entero de 12 dígitos.
                    String cardNumber = String.format("%", numberGenerated); //paso a String
                    int cardCvv = (int) (Math.random() * 900) + 100;
                    //Math.random() genera un número aleatorio decimal entre 0.0 y 1.0 (no incluyendo 1.0).
                    //Multiplicando Math.random() por 900 generamos un número aleatorio entre 0 y 899.
                    //Al sumarle 100, obtenemos un número aleatorio entre 100 y 999.
                    //Al convertir el resultado a un int, tenemos un número entero de 3 dígitos.
                    //Ten en cuenta que el resultado de Math.random() es un número decimal, por lo que al multiplicarlo por 900,
                    //obtendrás un número decimal entre 0.0 y 899.9... Para convertir este número decimal en un entero, se utiliza la operación de truncamiento al convertirlo a int.
                    CardType enumType = CardType.valueOf(typeCard);
                    CardColor enumColor = CardColor.valueOf(color);


                    Card cardGenerated = new Card(client.getFirstName() + " " + client.getFirstName(), enumType, enumColor, cardNumber, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
                    cardRepository.save(cardGenerated);
                    client.addCard(cardGenerated);
                    clientRepository.save(client);

                    return new ResponseEntity<>("Created Card", HttpStatus.CREATED);
                }
            } catch (IllegalArgumentException ex) { //Especifica una respuesta si se produce una excepción
                //IllegalArgumentException es una excepción de Java que indica que un método ha recibido un argumento que no es válido o es inapropiado para los fines de este método.
                //Esta excepción se utiliza normalmente cuando el procesamiento posterior en el método depende del argumento no válido y no puede continuar a menos que se proporcione un argumento adecuado en su lugar.
                return new ResponseEntity<>("Invalid or empty CardType or CardColor, Valid CardTypes: CREDIT || DEBIT ; valid CardColors: GOLD || SILVER || TITANIUM ", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Client already has 3 or more debitCards and 3 or more creditCards", HttpStatus.FORBIDDEN);
        }  return new ResponseEntity<>("Client already has 6 or more cards in total", HttpStatus.FORBIDDEN);
    }
}


