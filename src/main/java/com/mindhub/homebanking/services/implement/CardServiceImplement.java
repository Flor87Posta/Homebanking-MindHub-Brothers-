package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Override
    public void saveNewCard(Card card) {
        cardRepository.save(card);

    }

    @Override
    public Optional<Card> findByIdOptional(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }
}
