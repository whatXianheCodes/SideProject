package com.sideproject.casinogames;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Xianhe on 1/6/2015.
 */
public class Deck {
    private static final String TAG = "Deck";
    private int cardsLeft = 52;
    private final int DECK_SIZE = 52;
    Random rn = new Random();

    public List<Card> deck = new ArrayList<Card>();

    public Deck () {
        createDeck();
    }
    private void createDeck () {
        for (int i = 0; i < Card.Cardvalue.values().length; i++) {
            for (int j = 0; j < Card.Suit.values().length; j++) {
                Card card = new Card(Card.Cardvalue.values()[i], Card.Suit.values()[j]);
                deck.add(card);
            }
        }
    }

    public Card randomizedDraw () {
        if (cardsLeft == 0) {
            Log.d(TAG, "THERE ARE NO CARDS LEFT");
            return null;
        }
        int max = DECK_SIZE-1;
        int min = DECK_SIZE - cardsLeft;
        int randomIndex = rn.nextInt(max - min + 1) + min;
        Collections.swap(deck, min, randomIndex);
        Card card = deck.get(min);
        cardsLeft--;
        return card;
    }

    public void printDeck () {
        for (Card card: deck) {
            Log.d(TAG, card.getValue() + " of " + card.getSuit());
        }
    }

    public void newDeck () {
        deck.removeAll(deck);
        createDeck();
    }
}
