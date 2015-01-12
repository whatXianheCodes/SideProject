package com.sideproject.casinogames;

import java.util.Comparator;

/**
 * Created by Xianhe on 1/7/2015.
 */
public class Card implements Comparable<Card> {
    private Cardvalue value;
    private Suit suit;

    public enum Cardvalue {
        ACE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13);

        private int value;

        Cardvalue(int value) {
            this.value = value;
        }

        public int getCardValue() {
            return value;
        }

        public void setCardValue(int value) {
            this.value = value;
        }
    }

    public enum Suit {
        HEARTS(0),
        SPADES(1),
        CLUBS(2),
        DIAMONDS(3);

        private final int value;

        Suit(int value) {
            this.value = value;
        }

        public int getSuitValue () {
            return value;
        }
    }

    public Card (Cardvalue value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public Cardvalue getValue () {
        return this.value;
    }

    public void setValue(Cardvalue cardvalue){
        this.value = cardvalue;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int compareTo (Card card) {
       return this.getValue().getCardValue() - card.getValue().getCardValue();
    }
}
