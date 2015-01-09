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

    private void startGame () {
        deck = new Deck();
        Card cardDrawn;
        for (int i=0; i < START_HAND_SIZE; i ++) {
            cardDrawn = deck.randomizedDraw();
            hand.add(cardDrawn);
            drawCard(cardDrawn);
        }
        drawLine();
        for (int i=0; i<START_COMMUNITY_CARD_SIZE; i++) {
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

    private void sortCommunityCard () {
        Collections.sort(communityCard);
        for (Card card: communityCard) {
            Log.d(TAG, card.getValue() + " of " + card.getSuit());
        }
    }

    private void determinePokerHand () {
        sortCommunityCard();
    }

    private void drawCard (Card card) {
        TextView textView = new TextView(this);
        textView.setText(card.getValue() + " of " + card.getSuit());
        ((LinearLayout) findViewById(R.id.cards)).addView(textView);
    }

    private void drawLine () {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
        ((LinearLayout) findViewById(R.id.cards)).addView(v);
    }

    public void bet (View view) {
        //TODO: Will implement with money later on
    }

    public void check (View view) {
        if (communityCard.size() < 5) {
            Card cardDrawn = deck.randomizedDraw();
            communityCard.add(cardDrawn);
            drawCard(cardDrawn);
        }
        else {
            determinePokerHand();
            ((Button) findViewById(R.id.check)).setEnabled(false);
            ((Button) findViewById(R.id.bet)).setEnabled(false);
            ((Button) findViewById(R.id.fold)).setEnabled(false);
        }
    }

    public void fold (View view) {
        //TODO: Will implement with money later on
    }
}
