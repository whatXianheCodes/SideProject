package com.sideproject.casinogames.CardGames;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Xianhe on 1/13/2015.
 */
public class TexasHoldEm {
    private static final String TAG = "TexasHoldEm";

    List<Card> resultCards = new ArrayList<Card>();

    private final int RESULT_CARD_SIZE = 7;

    private int[] suits = {0, 0, 0, 0};
//    private int straightCount = 1;
    private int aceCount = 0;
//    private int straightFlushCount = 1;
//    private boolean possibleStraightFlush = false;
//
//    private int straightFlushHigh = 0;
    private int straightHigh = 0;
    private int flushHigh = 0;
    private int firstHighest = 0;
    private int secondHighest = 0;
    private int thirdHighest = 0;
    private int fourthHighest = 0;
    private int lowest = 0;
    private int pairHigh = 0;
    private int pairLow = 0;
    private int tripleHigh = 0;
    private int tripleLow = 0;
    private int fourOfAKind = 0;
    private Card.Suit flushSuit = null;

    private void createCustomCard () {
        Card card1 = new Card (Card.Cardvalue.ACE, Card.Suit.DIAMONDS);
        resultCards.add(card1);
        Card card2 = new Card (Card.Cardvalue.TWO, Card.Suit.HEARTS);
        resultCards.add(card2);
        Card card3 = new Card (Card.Cardvalue.SEVEN, Card.Suit.DIAMONDS);
        resultCards.add(card3);
        Card card4 = new Card (Card.Cardvalue.TWO, Card.Suit.HEARTS);
        resultCards.add(card4);
        Card card5 = new Card (Card.Cardvalue.KING, Card.Suit.DIAMONDS);
        resultCards.add(card5);
        Card card6 = new Card (Card.Cardvalue.SEVEN, Card.Suit.HEARTS);
        resultCards.add(card6);
        Card card7 = new Card (Card.Cardvalue.ACE, Card.Suit.HEARTS);
        resultCards.add(card7);
    }

    private void findFlushSuit(Card card) {
        for (Card.Suit suit : Card.Suit.values()) {
            if (suit == card.getSuit()) {
                int count = ++suits[suit.getSuitValue()];
                if (count >= 5) {
                    flushSuit = card.getSuit();
                }
            }
        }
    }

    private void initializeGame () {
        for (Card card : resultCards) {
            if (card.getValue() == Card.Cardvalue.ACE) {
                aceCount++;
                card.getValue().setCardValue(14);
            }
            findFlushSuit(card);
        }
    }

    public TexasHoldEm(List<Card> communityCard, List<Card> hand) {
//        resultCards.addAll(communityCard);
//        resultCards.addAll(hand);

        //TODO: comment this out once testing is done
        createCustomCard();
        initializeGame ();
        Collections.sort(resultCards);
    }

    private int findStraight(Card card, int lastHighCard, int straightCount) {
        int cardValue = card.getValue().getCardValue();
        if (cardValue - lastHighCard == 1) {
            straightCount++;
            if (straightCount >= 5) {
                straightHigh = cardValue;
            }
        }
        else if (cardValue - lastHighCard != 0) {
            straightCount = 1;
        }

        // special case where it's a straight starting with ace
        if (cardValue == 5 && straightCount == 4 && aceCount > 0) {
            straightCount++;
            straightHigh = cardValue;
        }
        return straightCount;
    }

    private int findDuplicate (int cardValue, int lastCard, int cardCount, int duplicateCounter) {
        // Last card, is special case since findDuplicate won't be called again
        if (cardValue == lastCard && cardCount == RESULT_CARD_SIZE) {
            duplicateCounter++;
        }
        if (cardValue == lastCard && cardCount != RESULT_CARD_SIZE) {
            duplicateCounter++;
        }
        else {
            if (duplicateCounter == 2) {
                pairLow = pairHigh;
                pairHigh = lastCard;

            }
            else if (duplicateCounter == 3) {
                tripleLow = tripleHigh;
                tripleHigh = lastCard;
            }
            else if (duplicateCounter >= 4) {
                fourOfAKind = lastCard;
            }
            duplicateCounter = 1;
        }
        return duplicateCounter;
    }

    private void kickerForPair () {
        int cardValue;
        for (Card card : resultCards) {
            cardValue = card.getValue().getCardValue();
            if (card.getValue().getCardValue() != pairHigh) {
                thirdHighest = secondHighest;
                secondHighest = firstHighest;
                firstHighest = cardValue;
            }
        }
    }

    private String determineRank() {
//        if (straightFlushHigh != 0) {
//            if (straightHigh == 14) {
//                return "Royal flush";
//            } else {
//                return "Straight Flush with high card " + straightHigh;
//            }
//        } else if (fourOfAKind != 0) {
//            return "Four of a kind " + fourOfAKind + " with kicker " + firstKicker;
//        } else if (tripleHigh != 0 && pairHigh != 0) {
//            return "Full house " + tripleHigh + " with " + pairHigh;
//        } else if (tripleHigh != 0 && tripleLow != 0) {
//            return "Full house " + tripleHigh + " with " + tripleLow;
//        } else if (flushHigh != 0) {
//            return "Flush with high card " + flushHigh;
//        } else if (straightHigh != 0) {
//            return "Straight with high card " + straightHigh;
//        } else if (tripleHigh != 0) {
//            return "Three of kind " + tripleHigh + " first kicker " + firstKicker + " second kicker " + secondKicker;
//        } else if (pairHigh != 0 && pairLow != 0) {
//            return "Two pair with high pair " + pairHigh + " low pair " + pairLow ;//+ " and kicker " + firstKicker;
//        } else
          if (pairHigh != 0) {
            return "Pair " + pairHigh + " first kicker " + firstHighest + " second kicker " + secondHighest + " third kicker " + thirdHighest;
        } else {
            return "High card ";
        }
    }

    public String determinePokerHand() {
        int cardValue;
        int lastCard = -1;
        int resultCardsCount = 0;
        int duplicateCounter = 1;
        int lastHighCard = -1;
        int straightCount = 1;

        for (Card card : resultCards) {
            resultCardsCount++;
            cardValue = card.getValue().getCardValue();
            straightCount = findStraight(card, lastHighCard, straightCount);
            duplicateCounter = findDuplicate(cardValue, lastCard, resultCardsCount, duplicateCounter);
            if (duplicateCounter == 1) {
                lastHighCard = cardValue;
            }
            if (card.getSuit() == flushSuit) {
                flushHigh = cardValue;
            }
            lastCard = cardValue;
        }
        //findKicker();
        return determineRank();
    }
}
