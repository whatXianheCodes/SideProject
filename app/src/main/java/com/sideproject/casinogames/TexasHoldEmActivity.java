package com.sideproject.casinogames;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TexasHoldEmActivity extends ActionBarActivity {

    private Deck deck;
    private static final String TAG = "TexasHoldEmActivity";
    private List<Card> hand = new ArrayList<Card>();
    private List<Card> communityCard = new ArrayList<Card>();
    List<Card> resultCards = new ArrayList<Card>();

    private final int START_HAND_SIZE = 2;
    private final int START_COMMUNITY_CARD_SIZE = 3;

    private List<Integer> pairs = new ArrayList<Integer>();
    private List<Integer> triples = new ArrayList<Integer>();
    private int[] suits = {0, 0, 0, 0};
    private int straightCount = 1;
    private int aceCount = 0;
    private boolean isFlush = false;
    private int duplicateCounter = 0;

    private int straightHigh = 0;
    private int fourOfAKind = 0;
    private int singleHigh = -1;
    private int flushHigh = 0;
    private int firstKicker = 0;
    private int secondKicker = 0;
    private int thirdKicker = 0;
    int pairHigh = 0;
    int pairLow = 0;
    int tripleHigh = 0;



    private void startGame() {
        deck = new Deck();
        Card cardDrawn;
        for (int i = 0; i < START_HAND_SIZE; i++) {
            cardDrawn = deck.randomizedDraw();
            hand.add(cardDrawn);
            drawCard(cardDrawn);
        }
        drawLine();
        for (int i = 0; i < START_COMMUNITY_CARD_SIZE; i++) {
            cardDrawn = deck.randomizedDraw();
            communityCard.add(cardDrawn);
            drawCard(cardDrawn);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texas_hold_em);
        //startGame();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_texas_hold_em, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void countSuit(Card card) {
        int count = 0;
        for (Card.Suit suit : Card.Suit.values()) {
            if (suit == card.getSuit()) {
                count = ++suits[suit.getSuitValue()];
                if (count >= 5) {
                    if (card.getValue() == card.getValue().ACE) {
                        flushHigh = 14;
                    } else {
                        flushHigh = card.getValue().getCardValue();
                    }
                    isFlush = true;
                    Log.d(TAG, card.getSuit().toString());
                }
            }
        }
    }

    private boolean findFourOfKind(int cardValue) {
        for (Integer triple : triples) {
            if (cardValue == triple) {
                triple = 0;
                fourOfAKind = cardValue;
                return true;
            }
        }
        return false;
    }

    private boolean findTriples(int cardValue) {
        for (Integer pair : pairs) {
            if (cardValue == pair) {
                pair = 0;
                triples.add(cardValue);
                Log.d(TAG, "triples with " + cardValue);
                return true;
            }
        }
        return false;
    }

    private boolean findPairs(int cardValue, int lastCard, boolean isTriple, boolean isFourOfKind) {
        if (!isFourOfKind && !isTriple && cardValue == lastCard) {
            pairs.add(cardValue);
            return true;
        }
        return false;
    }

    private void countStraight(Card card, int lastCard) {
        int cardValue = card.getValue().getCardValue();
        if (cardValue == 1) {
            aceCount++;
        }
        if (cardValue - singleHigh == 1) {
            straightCount++;
            if (straightCount >= 5) {
                straightHigh = cardValue;
            }
        }
        else if (lastCard == 13 && aceCount > 0 && straightCount >= 4) {
            straightCount++;
            straightHigh = 14;
        }
        else if (cardValue - singleHigh != 0) {
            straightCount = 1;
        }
    }

    private void findDuplicate (int cardValue, int lastCard) {
        if (cardValue == lastCard) {
            duplicateCounter++;
        }
        else if (duplicateCounter == 2) {
            if (cardValue > pairHigh) {
                pairLow = pairHigh;
                pairHigh = cardValue;
            } else if (cardValue > pairLow) {
                pairLow = cardValue;
            }
        }
        else if (duplicateCounter == 3) {
            tripleHigh = cardValue;
        }
        else if (duplicateCounter == 4) {
            fourOfAKind = cardValue;
        }
        duplicateCounter = 0;
    }

    private void findKicker () {
        int cardValue;
        for (Card card : resultCards) {
            cardValue = card.getValue().getCardValue();
            if (tripleHigh != 0 && cardValue != tripleHigh) {
                if (cardValue > firstKicker) {
                    secondKicker = firstKicker;
                    firstKicker = cardValue;
                }
                else if (cardValue > secondKicker) {
                    secondKicker = cardValue;
                }
            } else if (pairHigh != 0 && pairLow != 0
                    && cardValue != pairHigh && cardValue != pairLow) {
                if (cardValue > firstKicker) {
                    firstKicker = cardValue;
                }
            } else if (pairHigh != 0) {
                if (cardValue > firstKicker) {
                    thirdKicker = secondKicker;
                    secondKicker = firstKicker;
                    firstKicker = cardValue;
                }
                else if (cardValue > secondKicker) {
                    thirdKicker = secondKicker;
                    secondKicker = cardValue;
                }
                else if (cardValue > thirdKicker) {
                    firstKicker = cardValue;
                }
            }
        }
    }

    private String determineRank() {

        if (aceCount == 1) {
            singleHigh = 14;
        } else if (aceCount == 2) {
            pairHigh = 14;
        } else if (aceCount == 3) {
            tripleHigh = 14;
        } else if (aceCount == 4) {
            fourOfAKind = 14;
        }

        if (straightHigh != 0 && isFlush) {
            if (straightHigh == 14) {
                return "Royal flush";
            } else {
                return "Straight Flush with high card " + straightHigh;
            }
        } else if (tripleHigh != 0 && pairHigh != 0) {
            return "Full house " + tripleHigh + " with " + pairHigh;
        } else if (fourOfAKind != 0) {
            return "Four of a kind " + fourOfAKind;
        } else if (isFlush) {
            return "Flush with high card " + flushHigh;
        } else if (straightHigh != 0) {
            return "Straight with high card " + straightHigh;
        } else if (tripleHigh != 0) {
            return "Three of kind " + tripleHigh + "first kicker " + firstKicker + " second kicker " + secondKicker;
        } else if (pairHigh != 0 && pairLow != 0) {
            return "Two pair with high pair " + pairHigh + " low pair " + pairLow + " and kicker " + firstKicker;
        } else if (pairHigh != 0) {
            return "Pair " + pairHigh + "first kicker " + firstKicker + " second kicker " + secondKicker + " third kicker " + thirdKicker;
        } else {
            return "High card " + singleHigh;
        }
    }

    private String determinePokerHand() {

        int lastCard = -1;
        int cardValue = -1;
        int kicker = 0;

        List<Card> resultCards = new ArrayList<Card>();
//        resultCards.addAll(communityCard);
//        resultCards.addAll(hand);
//        Collections.sort(resultCards);
        Card card1 = new Card (Card.Cardvalue.TWO, Card.Suit.SPADES);
        resultCards.add(card1);
        Card card2 = new Card (Card.Cardvalue.TWO, Card.Suit.HEARTS);
        resultCards.add(card2);
        Card card3 = new Card (Card.Cardvalue.TWO, Card.Suit.HEARTS);
        resultCards.add(card3);
        Card card4 = new Card (Card.Cardvalue.JACK, Card.Suit.SPADES);
        resultCards.add(card4);
        Card card5 = new Card (Card.Cardvalue.QUEEN, Card.Suit.DIAMONDS);
        resultCards.add(card5);
        Card card6 = new Card (Card.Cardvalue.KING, Card.Suit.SPADES);
        resultCards.add(card6);
        Card card7 = new Card (Card.Cardvalue.KING, Card.Suit.SPADES);
        resultCards.add(card7);

        for (Card card : resultCards) {
            cardValue = card.getValue().getCardValue();
            countStraight(card, lastCard);
            countSuit(card);
            findDuplicate(cardValue, lastCard);

            if (pairHigh != 0 && tripleHigh != 0 && fourOfAKind!= 0) {
                singleHigh = cardValue;
            }
            lastCard = cardValue;
        }
        findKicker();
        return determineRank();
    }


    private void drawCard(Card card) {
        TextView textView = new TextView(this);
        textView.setText(card.getValue() + " of " + card.getSuit());
        ((LinearLayout) findViewById(R.id.cards)).addView(textView);
    }

    private void drawLine() {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
        ((LinearLayout) findViewById(R.id.cards)).addView(v);
    }

    public void bet(View view) {
        //TODO: Will implement with money later on
    }

    public void check(View view) {
        Log.d(TAG, determinePokerHand());
        //determinePokerHand();
        resultCards.clear();
//        if (communityCard.size() < 5) {
//            Card cardDrawn = deck.randomizedDraw();
//            communityCard.add(cardDrawn);
//            drawCard(cardDrawn);
//        } else {
//            determinePokerHand();
//            ((Button) findViewById(R.id.check)).setEnabled(false);
//            ((Button) findViewById(R.id.bet)).setEnabled(false);
//            ((Button) findViewById(R.id.fold)).setEnabled(false);
//        }
    }

    public void fold(View view) {
        //TODO: Will implement with money later on
    }
}
