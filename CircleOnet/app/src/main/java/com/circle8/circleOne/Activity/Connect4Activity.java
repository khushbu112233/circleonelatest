package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityConnect4Binding;
import com.squareup.picasso.Picasso;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class Connect4Activity extends AppCompatActivity {

    String level = "";
    String profile, profileName = "";
    String connectLevel = "";
    String userName1 = "", userName2 = "", userName3 = "", userName4 = "", userName5 = "", userName6 = "", userName7 = "";
    String userPhoto1 = "", userPhoto2 = "", userPhoto3 = "", userPhoto4 = "", userPhoto5 = "", userPhoto6 = "", userPhoto7 = "";
    String userProfileId1 = "", userProfileId2 = "", userProfileId3 = "", userProfileId4 = "", userProfileId5 = "",
            userProfileId6 = "", userProfileId7 = "";
    Stripe stripe;
    Card card;
    Token tok;
    AlertDialog alertDialog;
    String numberOnCard, nameOnCard, exYearOnCard, exMonthOnCard, cvvOnCard, mobileNoOnCard, strToken;
    ActivityConnect4Binding activityConnect4Binding;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityConnect4Binding = DataBindingUtil.setContentView(this, R.layout.activity_connect4);
        activity = this;
        Utility.freeMemory();
        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        profile = intent.getStringExtra("profile");
        profileName = intent.getStringExtra("profileName");
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

        activityConnect4Binding.txtProfileName.setText(profileName);

        try {
            Utility.freeMemory();
            if (profile.equalsIgnoreCase("") || profile.equalsIgnoreCase("null")) {
                activityConnect4Binding.ivImage1.setImageResource(R.drawable.usr_white);
            } else {
                Picasso.with(getApplicationContext()).load(profile).resize(300, 300).onlyScaleDown().skipMemoryCache().into(activityConnect4Binding.ivImage1);
            }
        } catch (Exception e) {
            activityConnect4Binding.ivImage1.setImageResource(R.drawable.usr_white);
        }

           /* try
            {
                Picasso.with(getApplicationContext()).load(profile).placeholder(R.drawable.usr).into(ivImage1);
            }
            catch (Exception e) {
                ivImage1.setImageResource(R.drawable.usr);
            }*/

        if (level.equals("0")) {
            Utility.freeMemory();
            //  txtCongratulations.setVisibility(View.GONE);
            activityConnect4Binding.txtAsk.setVisibility(View.GONE);
            activityConnect4Binding.txtLink.setText("You have no established connections");
            activityConnect4Binding.imgLevel1.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        } else if (level.equals("1")) {
            // txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtAsk.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtLink.setText("you are now 1 connection away");
            activityConnect4Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        } else if (level.equals("2")) {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtAsk.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtLink.setText("you are now 2 connections away");
            activityConnect4Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        } else if (level.equals("3")) {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtAsk.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtLink.setText("you are now 3 connections away");
            activityConnect4Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        } else if (level.equals("4")) {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtAsk.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtLink.setText("you are now 4 connections away");
            activityConnect4Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        } else if (level.equals("5")) {
            // txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtAsk.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtLink.setText("you are now 5 connections away");
            activityConnect4Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        } else if (level.equals("6")) {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtAsk.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtLink.setText("you are now 6 connections away");
            activityConnect4Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        } else if (level.equals("7")) {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtAsk.setVisibility(View.VISIBLE);
            activityConnect4Binding.txtLink.setText("you are now 7 connections away");
            activityConnect4Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.VISIBLE);
        } else {
            activityConnect4Binding.txtAsk.setVisibility(View.GONE);
            activityConnect4Binding.txtLink.setText("You have no established connections");
            activityConnect4Binding.imgLevel1.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel2.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel3.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel4.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel5.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel6.setVisibility(View.INVISIBLE);
            activityConnect4Binding.imgLevel7.setVisibility(View.INVISIBLE);
        }


        activityConnect4Binding.txtAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(), Connect5Activity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                go.putExtra("profileName", profileName);
                go.putExtra("connectLevel", connectLevel);
                go.putExtra("userName1", userName1);
                go.putExtra("userPhoto1", userPhoto1);
                go.putExtra("userProfileId1", userProfileId1);
                go.putExtra("userName2", userName2);
                go.putExtra("userPhoto2", userPhoto2);
                go.putExtra("userProfileId2", userProfileId2);
                go.putExtra("userName3", userName3);
                go.putExtra("userPhoto3", userPhoto3);
                go.putExtra("userProfileId3", userProfileId3);
                go.putExtra("userName4", userName4);
                go.putExtra("userPhoto4", userPhoto4);
                go.putExtra("userProfileId4", userProfileId4);
                go.putExtra("userName5", userName5);
                go.putExtra("userPhoto5", userPhoto5);
                go.putExtra("userProfileId5", userProfileId5);
                go.putExtra("userName6", userName6);
                go.putExtra("userPhoto6", userPhoto6);
                go.putExtra("userProfileId6", userProfileId6);
                go.putExtra("userName7", userName7);
                go.putExtra("userPhoto7", userPhoto7);
                go.putExtra("userProfileId7", userProfileId7);
                startActivity(go);
                finish();


                   /* Intent go = new Intent(getApplicationContext(), StripeActivity.class);
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
                    finish();*/

/*                    alertDialog = new AlertDialog.Builder(Connect4Activity.this).create();
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.stripe_payment_screen1, null);

                    LinearLayout llCardValues = (LinearLayout)dialogView.findViewById(R.id.llCardValues);
                    LinearLayout llPackageDetails = (LinearLayout)dialogView.findViewById(R.id.llPackageDetails);

                    final EditText etCardNumber = (EditText)dialogView.findViewById(R.id.etCardNumber);
                    final EditText etCardHolderName = (EditText)dialogView.findViewById(R.id.etCardHolderName);
                    final EditText etExMonth = (EditText)dialogView.findViewById(R.id.etExMonth);
                    final EditText etExYear = (EditText)dialogView.findViewById(R.id.etExYear);
                    final EditText etSecurityCode = (EditText)dialogView.findViewById(R.id.etSecurityCode);
                    final EditText etMobileNumber = (EditText)dialogView.findViewById(R.id.etMobileNumber);
                    TextView tvPay = (TextView)dialogView.findViewById(R.id.tvPay);
                    TextView tvCancel = (TextView)dialogView.findViewById(R.id.tvCancel);

                    tvPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            numberOnCard = etCardNumber.getText().toString();
                            nameOnCard = etCardHolderName.getText().toString();
                            exYearOnCard = etExYear.getText().toString();
                            exMonthOnCard = etExMonth.getText().toString();
                            cvvOnCard = etSecurityCode.getText().toString();
                            mobileNoOnCard = etMobileNumber.getText().toString();

                            if (numberOnCard.isEmpty())
                            {
                                Toast.makeText(Connect4Activity.this,"Enter Card No.",Toast.LENGTH_SHORT).show();
                            }
                            else if (nameOnCard.isEmpty())
                            {
                                Toast.makeText(Connect4Activity.this,"Enter Holder Name",Toast.LENGTH_SHORT).show();
                            }
                            else if (exMonthOnCard.isEmpty())
                            {
                                Toast.makeText(Connect4Activity.this,"Enter Expiry Month",Toast.LENGTH_SHORT).show();
                            }
                            else if (exYearOnCard.isEmpty())
                            {
                                Toast.makeText(Connect4Activity.this,"Enter Expiry Month",Toast.LENGTH_SHORT).show();
                            }
                            else if (cvvOnCard.isEmpty())
                            {
                                Toast.makeText(Connect4Activity.this,"Enter CVV No.",Toast.LENGTH_SHORT).show();
                            }
                            else if (mobileNoOnCard.isEmpty())
                            {
                                Toast.makeText(Connect4Activity.this,"Enter Mobile No.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                cardPayment();
                                alertDialog.dismiss();
                            }
                        }
                    });

                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.setView(dialogView);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
                    alertDialog.show();*/
            }
        });

        activityConnect4Binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                finish();
            }
        });
    }

    public static void kill() {
        activity.finish();
    }


    public void cardPayment() {
        card = new Card
                (
                        numberOnCard,
                        Integer.valueOf(exMonthOnCard),
                        Integer.valueOf(exYearOnCard),
                        cvvOnCard
                );

        card.setCurrency("sgd");
        card.setName(nameOnCard);

        stripe.createToken(card, "pk_live_d0uXEesOC2Qg5919ul4t7Ocl", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
                strToken = token.getId();
                //  new StripeCharge(token.getId()).execute();
//                new HttpAsyncTokenTask().execute("https://circle8.asia/Checkout/pay");
//                new HttpAsyncRequestTask().execute(Utility.BASE_URL+"Physical_Card/Order");

                Intent go = new Intent(getApplicationContext(), Connect5Activity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                go.putExtra("connectLevel", connectLevel);
                go.putExtra("userName1", userName1);
                go.putExtra("userPhoto1", userPhoto1);
                go.putExtra("userProfileId1", userProfileId1);
                go.putExtra("userName2", userName2);
                go.putExtra("userPhoto2", userPhoto2);
                go.putExtra("userProfileId2", userProfileId2);
                go.putExtra("userName3", userName3);
                go.putExtra("userPhoto3", userPhoto3);
                go.putExtra("userProfileId3", userProfileId3);
                go.putExtra("userName4", userName4);
                go.putExtra("userPhoto4", userPhoto4);
                go.putExtra("userProfileId4", userProfileId4);
                go.putExtra("userName5", userName5);
                go.putExtra("userPhoto5", userPhoto5);
                go.putExtra("userProfileId5", userProfileId5);
                go.putExtra("userName6", userName6);
                go.putExtra("userPhoto6", userPhoto6);
                go.putExtra("userProfileId6", userProfileId6);
                go.putExtra("userName7", userName7);
                go.putExtra("userPhoto7", userPhoto7);
                go.putExtra("userProfileId7", userProfileId7);
                startActivity(go);
                finish();

                alertDialog.cancel();
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }

}


