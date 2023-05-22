package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

import java.util.Optional;

public interface CardService {


    void saveNewCard(Card card);

    Card findByNumber(String number);

    void delete(Card card);

    boolean existsByNumber(String number);


}
