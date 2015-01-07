package com.sideproject.casinogames;

/**
 * Created by Xianhe on 1/7/2015.
 */
public class Card {
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

        private final int value;

        Cardvalue(int value) {
            this.value = value;
        }

        public int getCardValue() {
            return value;
        }
    }

    public enum Suit {
        HEARTS,
        SPADES,
        CLUBS,
        DIAMONDS;
    }

    public Card (Cardvalue value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public Cardvalue getValue () {
        return this.value;
    }

    public Suit getSuit() {
        return this.suit;
    }
}
