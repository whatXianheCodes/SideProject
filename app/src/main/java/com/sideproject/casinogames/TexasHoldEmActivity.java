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

    private final int START_HAND_SIZE = 2;
    private final int START_COMMUNITY_CARD_SIZE = 3;

    private List<Integer> pairs = new ArrayList<Integer>();
    private List<Integer> triples = new ArrayList<Integer>();
    private int[] suits = {0, 0, 0, 0};
    private int straightCount = 0;

    private int straightHigh = 0;
    private int fourOfAKind = 0;
    private int single = 0;
    private int flushHigh = 0;
    private int aceCount = 0;
    private boolean isFlush = false;


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
        startGame();
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
        if (cardValue - single == 1) {
            straightCount++;
            if (straightCount >= 5) {
                straightHigh = cardValue;
            }
        } else {
            straightCount = 0;
        }
        if (lastCard == 13 && aceCount > 0 && straightCount >= 4) {
            straightCount++;
            straightHigh = 14;
        }
    }

    private String determineRank(boolean isPair, boolean isTriple, boolean isFourOfKind) {
        int pairHigh = 0;
        int pairLow = 0;
        int tripleHigh = 0;
        int kicker = single;

        if (aceCount == 1) {
            single = 14;
        } else if (aceCount == 2) {
            pairHigh = 14;
        } else if (aceCount == 3) {
            tripleHigh = 14;
        } else if (aceCount == 4) {
            fourOfAKind = 14;
        }

        for (Integer pair : pairs) {
            if (pair > pairHigh) {
                pairLow = pairHigh;
                pairHigh = pair;
            } else if (pair > pairLow) {
                pairLow = pair;
            }
        }
        for (Integer triple : triples) {
            if (triple > tripleHigh) {
                tripleHigh = triple;
            }
        }

        if (straightCount == 5 && isFlush) {
            if (straightHigh == 14) {
                return "Royal flush";
            } else {
                return "Straight Flush with high card " + straightHigh;
            }
        } else if (isTriple && isPair) {
            return "Full house " + tripleHigh + " with " + pairHigh;
        } else if (isFourOfKind) {
            return "Four of a kind " + fourOfAKind;
        } else if (isFlush) {
            return "Flush with high card " + flushHigh;
        } else if (straightCount == 5) {
            return "Straight with high card " + straightHigh;
        } else if (isTriple) {
            return "Three of kind " + tripleHigh;
        } else if (isPair && pairLow != 0 && pairHigh != 0) {
            return "Two pair with high pair " + pairHigh + " low pair " + pairLow + "and single " + single;
        } else if (isPair) {
            return "Pair " + pairHigh + "with kicker " + single;
        } else {
            return "High card " + single;
        }
    }

    private String determinePokerHand() {

        int lastCard = -1;
        int cardValue = 0;
        boolean isTriple = false;
        boolean isFourOfKind = false;
        boolean isPair = false;

        List<Card> resultCards = new ArrayList<Card>();
        resultCards.addAll(communityCard);
        resultCards.addAll(hand);
        Collections.sort(resultCards);

        for (Card card : resultCards) {
            cardValue = card.getValue().getCardValue();
            countStraight(card, lastCard);
            countSuit(card);
            isFourOfKind = findFourOfKind(cardValue);
            isTriple = findTriples(cardValue);
            isPair = findPairs(cardValue, lastCard, isTriple, isFourOfKind);
            if (!isFourOfKind && !isTriple && !isPair) {
                single = cardValue;
            }
            lastCard = cardValue;
        }

        return determineRank(isPair, isTriple, isFourOfKind);
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
        if (communityCard.size() < 5) {
            Card cardDrawn = deck.randomizedDraw();
            communityCard.add(cardDrawn);
            drawCard(cardDrawn);
        } else {
            determinePokerHand();
            ((Button) findViewById(R.id.check)).setEnabled(false);
            ((Button) findViewById(R.id.bet)).setEnabled(false);
            ((Button) findViewById(R.id.fold)).setEnabled(false);
        }
    }

    public void fold(View view) {
        //TODO: Will implement with money later on
    }
}
