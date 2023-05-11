package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

import java.util.Optional;

public interface CardService {


    void saveNewCard(Card card);

    Optional<Card> findByIdOptional(Long id);
    void delete(Card card);


}
