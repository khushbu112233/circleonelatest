package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

public class StripeActivity extends AppCompatActivity
{
    Stripe stripe;
   /* Integer amount;
    String name;*/
    Card card;
    Token tok;
    String level = "";
    String profile;

    String connectLevel = "";
    String userName1 = "", userName2 = "", userName3 = "", userName4 = "", userName5 = "", userName6 = "", userName7 = "";
    String userPhoto1 = "", userPhoto2 = "", userPhoto3 = "", userPhoto4 = "", userPhoto5 = "", userPhoto6 = "", userPhoto7 = "" ;
    String userProfileId1 = "", userProfileId2 = "", userProfileId3 = "", userProfileId4 = "", userProfileId5 = "",
            userProfileId6 = "", userProfileId7 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_1);
        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        profile = intent.getStringExtra("profile");
        connectLevel = intent.getStringExtra("connectLevel");
        userName1 = intent.getStringExtra("userName1");
        userPhoto1 = intent.getStringExtra("userPhoto1");
        userProfileId1 = intent.getStringExtra("userProfileId1");
        userName2 = intent.getStringExtra("userName2");
        userPhoto2 = intent.getStringExtra("userPhoto2");
        userProfileId2 = intent.getStringExtra("userProfileId2");
        userName3 = intent.getStringExtra("userName3");
        userPhoto3 = intent.getStringExtra("userPhoto3");
        userProfileId3 = intent.getStringExtra("userProfileId3");
        userName4 = intent.getStringExtra("userName4");
        userPhoto4 = intent.getStringExtra("userPhoto4");
        userProfileId4 = intent.getStringExtra("userProfileId4");
        userName5 = intent.getStringExtra("userName5");
        userPhoto5 = intent.getStringExtra("userPhoto5");
        userProfileId5 = intent.getStringExtra("userProfileId5");
        userName6 = intent.getStringExtra("userName6");
        userPhoto6 = intent.getStringExtra("userPhoto6");
        userProfileId6 = intent.getStringExtra("userProfileId6");
        userName7 = intent.getStringExtra("userName7");
        userPhoto7 = intent.getStringExtra("userPhoto7");
        userProfileId7 = intent.getStringExtra("userProfileId7");

        try
        {
            stripe = new Stripe("pk_test_6fZCC6Gu2kwYLUQxJhGte65l");
        }
        catch (AuthenticationException e)
        {
            e.printStackTrace();
        }
    }


    public void submitCard(View view) {
        // TODO: replace with your own test key
        TextView cardNumberField = (TextView) findViewById(R.id.cardNumber);
        TextView monthField = (TextView) findViewById(R.id.month);
        TextView yearField = (TextView) findViewById(R.id.year);
        TextView cvcField = (TextView) findViewById(R.id.cvc);

        card = new Card(
                cardNumberField.getText().toString(),
                Integer.valueOf(monthField.getText().toString()),
                Integer.valueOf(yearField.getText().toString()),
                cvcField.getText().toString()
        );

        card.setCurrency("usd");
        card.setName("Theodhor Pandeli");
        card.setAddressZip("1000");
        /*
        card.setNumber(4242424242424242);
        card.setExpMonth(12);
        card.setExpYear(19);
        card.setCVC("123");
        */

        stripe.createToken(card, "pk_test_6fZCC6Gu2kwYLUQxJhGte65l", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
              //  new StripeCharge(token.getId()).execute();

                Intent go = new Intent(getApplicationContext(), Connect5Activity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                go.putExtra("connectLevel", connectLevel);
                go.putExtra("userName1",userName1);
                go.putExtra("userPhoto1",userPhoto1);
                go.putExtra("userProfileId1",userProfileId1);
                go.putExtra("userName2",userName2);
                go.putExtra("userPhoto2",userPhoto2);
                go.putExtra("userProfileId2",userProfileId2);
                go.putExtra("userName3",userName3);
                go.putExtra("userPhoto3",userPhoto3);
                go.putExtra("userProfileId3",userProfileId3);
                go.putExtra("userName4",userName4);
                go.putExtra("userPhoto4",userPhoto4);
                go.putExtra("userProfileId4",userProfileId4);
                go.putExtra("userName5",userName5);
                go.putExtra("userPhoto5",userPhoto5);
                go.putExtra("userProfileId5",userProfileId5);
                go.putExtra("userName6",userName6);
                go.putExtra("userPhoto6",userPhoto6);
                go.putExtra("userProfileId6",userProfileId6);
                go.putExtra("userName7",userName7);
                go.putExtra("userPhoto7",userPhoto7);
                go.putExtra("userProfileId7",userProfileId7);
                startActivity(go);
                finish();
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }

}
