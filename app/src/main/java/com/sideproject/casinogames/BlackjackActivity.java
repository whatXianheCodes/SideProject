package com.sideproject.casinogames;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class BlackjackActivity extends ActionBarActivity {

    private static final String TAG = "BlackjackActivity";
    private List<Card> hand = new ArrayList<Card>();
    Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackjack);
        deck = new Deck();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blackjack, menu);
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

    private int endTurn() {
        int numberofAce = 0;
        int totalScore = 0;
        for (Card card: hand) {
            if (card.getValue().getCardValue() == 1) {
                numberofAce++;
            }
            else if( card.getValue().getCardValue() > 10) {
                totalScore += 10;
            }
            else {
                totalScore += card.getValue().getCardValue();
            }
        }
        for (int i = 0; i < numberofAce; i++) {
            if ((totalScore + 11) > 21) {
                totalScore +=1;
            }
            else {
                totalScore +=11;
            }
        }
        return totalScore;
    }

    private void displayToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void drawHand (Card card) {
        TextView textView = new TextView(this);
        textView.setText(card.getValue() + " of " + card.getSuit());
        ((LinearLayout) findViewById(R.id.hand)).addView(textView);
    }

    public void stand (View view) {
        int finalScore = endTurn();
        if (finalScore > 21) {
            displayToastMessage("You went over 21");
        }
        else {
            displayToastMessage("Your score is " + finalScore);
        }
    }

    public void hit (View view) {
        Card cardDrawn = deck.randomizedDraw();
        hand.add(cardDrawn);
        drawHand(cardDrawn);
    }
}
