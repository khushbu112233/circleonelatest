package com.circle8.circleOne.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.CardPrize;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.LuckyDrawLayoutBinding;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LuckyDrawActivity extends AppCompatActivity {
    LuckyDrawLayoutBinding luckyDrawLayoutBinding;
    ArrayList<String> stringArrayList = new ArrayList<>();
    int pos=0;
    CountDownTimer timer;
    int count=0;
    LoginSession session;
    public static String  UserId= "";
    int[] solutionArray = { 1, 2, 3, 4, 5, 6, 7, 8 };
    ArrayList<CardPrize> allCards = new ArrayList<>();
    int id = 0;
    ArrayList<Integer> prizeIdList= new ArrayList<>();
    String[] pointArray = {"CircleOne Points","Apple ipad pro","NFC Name Card","CircleOne Points","CircleOne Points","Amazon Kindle","Earphone","CircleOne Points"};

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        luckyDrawLayoutBinding = DataBindingUtil.setContentView(this,R.layout.lucky_draw_layout);

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        new HttpAsyncTask().execute(Utility.BASE_URL+"RewardsGame/Refresh");
        new HttpAsyncTaskWininingPrize().execute(Utility.BASE_URL+"RewardsGame/WinningPrize");
        stringArrayList.add("test fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test1 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test2 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test3 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test4 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test5 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.shuffle);

      //  manageBlinkEffect();

        final Animation animation = new AlphaAnimation((float) 0.5, 0);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        luckyDrawLayoutBinding.imglogo.startAnimation(animation);

        luckyDrawLayoutBinding.easyFlipView1.setFlipDuration(1500);
        luckyDrawLayoutBinding.easyFlipView2.setFlipDuration(1500);
        luckyDrawLayoutBinding.easyFlipView3.setFlipDuration(1500);
        luckyDrawLayoutBinding.easyFlipView4.setFlipDuration(1500);
        luckyDrawLayoutBinding.easyFlipView5.setFlipDuration(1500);
        luckyDrawLayoutBinding.easyFlipView6.setFlipDuration(1500);
        luckyDrawLayoutBinding.easyFlipView7.setFlipDuration(1500);
        luckyDrawLayoutBinding.easyFlipView8.setFlipDuration(1500);

        luckyDrawLayoutBinding.txtDesc.setText(stringArrayList.get(pos));

        timer = new CountDownTimer(4000, 4000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                try{
                    if(pos==stringArrayList.size()-1)
                    {
                        pos=0;
                    }else {
                        pos++;
                    }
                    if(pos==0)
                    {
                        luckyDrawLayoutBinding.imgLeft.setVisibility(View.GONE);
                        luckyDrawLayoutBinding.imgRight.setVisibility(View.VISIBLE);
                    }else if(pos==1)
                    {
                        luckyDrawLayoutBinding.imgLeft.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.imgRight.setVisibility(View.VISIBLE);
                    }else if(pos==stringArrayList.size()-1)
                    {
                        luckyDrawLayoutBinding.imgLeft.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.imgRight.setVisibility(View.GONE);
                    }

                    luckyDrawLayoutBinding.txtDesc.setText(stringArrayList.get(pos));
                    timer.start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
        luckyDrawLayoutBinding.includePrize.rtlRefresh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                mp.start();
                count = 0;
                prizeIdList = new ArrayList<>();

                luckyDrawLayoutBinding.easyFlipView1.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView1.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView1.setFlipOnTouch(true);
                luckyDrawLayoutBinding.easyFlipView1.setEnabled(true);

                luckyDrawLayoutBinding.easyFlipView2.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView2.setEnabled(true);
                luckyDrawLayoutBinding.easyFlipView2.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView2.setFlipOnTouch(true);

                luckyDrawLayoutBinding.easyFlipView3.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView3.setEnabled(true);
                luckyDrawLayoutBinding.easyFlipView3.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView3.setFlipOnTouch(true);

                luckyDrawLayoutBinding.easyFlipView4.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView4.setEnabled(true);
                luckyDrawLayoutBinding.easyFlipView4.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView4.setFlipOnTouch(true);

                luckyDrawLayoutBinding.easyFlipView5.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView5.setEnabled(true);
                luckyDrawLayoutBinding.easyFlipView5.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView5.setFlipOnTouch(true);

                luckyDrawLayoutBinding.easyFlipView6.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView6.setEnabled(true);
                luckyDrawLayoutBinding.easyFlipView6.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView6.setFlipOnTouch(true);

                luckyDrawLayoutBinding.easyFlipView7.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView7.setEnabled(true);
                luckyDrawLayoutBinding.easyFlipView7.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView7.setFlipOnTouch(true);

                luckyDrawLayoutBinding.easyFlipView8.setClickable(true);
                luckyDrawLayoutBinding.easyFlipView8.setEnabled(true);
                luckyDrawLayoutBinding.easyFlipView8.setFlipEnabled(true);
                luckyDrawLayoutBinding.easyFlipView8.setFlipOnTouch(true);

                if (luckyDrawLayoutBinding.easyFlipView1.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView1.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView1.flipTheView(true);
                }

                if (luckyDrawLayoutBinding.easyFlipView2.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView2.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView2.flipTheView(true);
                }

                if (luckyDrawLayoutBinding.easyFlipView3.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView3.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView3.flipTheView(true);
                }

                if (luckyDrawLayoutBinding.easyFlipView4.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView4.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView4.flipTheView(true);
                }

                if (luckyDrawLayoutBinding.easyFlipView5.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView5.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView5.flipTheView(true);
                }

                if (luckyDrawLayoutBinding.easyFlipView6.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView6.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView6.flipTheView(true);
                }

                if (luckyDrawLayoutBinding.easyFlipView7.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView7.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView7.flipTheView(true);
                }

                if (luckyDrawLayoutBinding.easyFlipView8.isBackSide()) {

                    luckyDrawLayoutBinding.easyFlipView8.flipTheView();
                    luckyDrawLayoutBinding.easyFlipView8.flipTheView(true);
                }

                Collections.shuffle(allCards);
                Log.e("prizeIdList",""+prizeIdList);

                ObjectAnimator flip1 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView1, "rotationY", 180f, 360f);
                flip1.setDuration(1500);
                flip1.start();
                ObjectAnimator flip2 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView2, "rotationY", 180f, 360f);
                flip2.setDuration(1500);
                flip2.start();
                ObjectAnimator flip3 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView3, "rotationY", 180f, 360f);
                flip3.setDuration(1500);
                flip3.start();
                ObjectAnimator flip4 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView4, "rotationY", 180f, 360f);
                flip4.setDuration(1500);
                flip4.start();
                ObjectAnimator flip5 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView5, "rotationY", 180f, 360f);
                flip5.setDuration(1500);
                flip5.start();
                ObjectAnimator flip6 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView6, "rotationY", 180f, 360f);
                flip6.setDuration(1500);
                flip6.start();
                ObjectAnimator flip7 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView7, "rotationY", 180f, 360f);
                flip7.setDuration(1500);
                flip7.start();
                ObjectAnimator flip8 = ObjectAnimator.ofFloat(luckyDrawLayoutBinding.easyFlipView8, "rotationY", 180f, 360f);
                flip8.setDuration(1500);
                flip8.start();
            }
        });
        luckyDrawLayoutBinding.imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos--;
                if(pos==0)
                {
                    luckyDrawLayoutBinding.imgLeft.setVisibility(View.INVISIBLE);
                }else if(pos==stringArrayList.size()-1)
                {
                    luckyDrawLayoutBinding.imgRight.setVisibility(View.VISIBLE);
                }else
                {
                    luckyDrawLayoutBinding.imgLeft.setVisibility(View.VISIBLE);
                    luckyDrawLayoutBinding.imgRight.setVisibility(View.VISIBLE);
                }
                luckyDrawLayoutBinding.txtDesc.setText(stringArrayList.get(pos));
            }
        });


        luckyDrawLayoutBinding.imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos++;
                if(pos==stringArrayList.size()-1)
                {
                    luckyDrawLayoutBinding.imgRight.setVisibility(View.INVISIBLE);
                }else if(pos==1)
                {
                    luckyDrawLayoutBinding.imgLeft.setVisibility(View.VISIBLE);
                }else
                {
                    luckyDrawLayoutBinding.imgLeft.setVisibility(View.VISIBLE);
                    luckyDrawLayoutBinding.imgRight.setVisibility(View.VISIBLE);
                }
                luckyDrawLayoutBinding.txtDesc.setText(stringArrayList.get(pos));
            }
        });


        luckyDrawLayoutBinding.easyFlipView1.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(0).getPrize_ID()));

            }
        });
        luckyDrawLayoutBinding.easyFlipView2.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(1).getPrize_ID()));
            }
        });
        luckyDrawLayoutBinding.easyFlipView3.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(2).getPrize_ID()));
            }
        });
        luckyDrawLayoutBinding.easyFlipView4.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(3).getPrize_ID()));
            }
        });
        luckyDrawLayoutBinding.easyFlipView5.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(4).getPrize_ID()));
            }
        });
        luckyDrawLayoutBinding.easyFlipView6.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(5).getPrize_ID()));
            }
        });
        luckyDrawLayoutBinding.easyFlipView7.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(6).getPrize_ID()));
            }
        });
        luckyDrawLayoutBinding.easyFlipView8.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);
                flipView.setClickable(false);
                flipView.setEnabled(false);
                prizeIdList.add(Integer.parseInt(allCards.get(7).getPrize_ID()));
            }
        });
    }

    private void manageBlinkEffect() {
        ObjectAnimator anim = ObjectAnimator.ofInt(luckyDrawLayoutBinding.imglogo, "backgroundColor", Color.WHITE, Color.RED,
                Color.WHITE);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }

/*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void shuffleArray(ArrayList<CardPrize> ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.size() - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            CardPrize a = ar.get(index);
            ar.get(index) = ar.get(i);
            ar.get(i) = a;
        }
    }
*/

    public void countMaintain(int count)
    {
        if(count>=3) {
            luckyDrawLayoutBinding.easyFlipView1.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView2.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView3.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView4.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView5.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView6.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView7.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView8.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView1.setFlipOnTouch(false);
            luckyDrawLayoutBinding.easyFlipView2.setFlipOnTouch(false);
            luckyDrawLayoutBinding.easyFlipView3.setFlipOnTouch(false);
            luckyDrawLayoutBinding.easyFlipView4.setFlipOnTouch(false);
            luckyDrawLayoutBinding.easyFlipView5.setFlipOnTouch(false);
            luckyDrawLayoutBinding.easyFlipView6.setFlipOnTouch(false);
            luckyDrawLayoutBinding.easyFlipView7.setFlipOnTouch(false);
            luckyDrawLayoutBinding.easyFlipView8.setFlipOnTouch(false);

        }
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(LuckyDrawActivity.this);
            dialog.setMessage("Fetching event...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("userid",   UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Utility.POST2(urls[0],jsonObject );
        }
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("response",""+result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);
                String message = response.getString("message");
                String success = response.getString("success");
                String userid = response.getString("userid");
                String No_Of_Tokens = response.getString("No_Of_Tokens");
                String Rewards_Points_Remain = response.getString("Rewards_Points_Remain");
                luckyDrawLayoutBinding.includePrize.txtTokenCount.setText(No_Of_Tokens);
                if(success.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("prizes_8");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject iCon = jsonArray.getJSONObject(i);
                        CardPrize cardPrizeModel = new CardPrize();
                        cardPrizeModel.setPrize_ID(iCon.getString("Prize_ID"));
                        cardPrizeModel.setPrize_Name(iCon.getString("Prize_Name"));
                        cardPrizeModel.setPrize_Image(iCon.getString("Prize_Image"));
                        cardPrizeModel.setPrize_Available_Count(iCon.getString("Prize_Available_Count"));
                        cardPrizeModel.setPrize_Won_Count(iCon.getString("Prize_Won_Count"));
                        cardPrizeModel.setTotal_Prize_Count(iCon.getString("Total_Prize_Count"));
                        allCards.add(cardPrizeModel);

                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    private class HttpAsyncTaskWininingPrize extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(LuckyDrawActivity.this);
            dialog.setMessage("Fetching Prize...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("userid", UserId);
                // jsonObject.accumulate("Prize_ID",prizeIdList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Utility.POST2(urls[0],jsonObject );
        }
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("response",""+result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);
                String message = response.getString("message");
                String success = response.getString("success");
                String userid = response.getString("userid");
                String No_Of_Tokens = response.getString("No_Of_Tokens");
                String Rewards_Points_Remain = response.getString("Rewards_Points_Remain");
                luckyDrawLayoutBinding.includePrize.txtTokenCount.setText(No_Of_Tokens);
                if(success.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("prizes_8");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject iCon = jsonArray.getJSONObject(i);
                        CardPrize cardPrizeModel = new CardPrize();
                        cardPrizeModel.setPrize_ID(iCon.getString("Prize_ID"));
                        cardPrizeModel.setPrize_Name(iCon.getString("Prize_Name"));
                        cardPrizeModel.setPrize_Image(iCon.getString("Prize_Image"));
                        cardPrizeModel.setPrize_Available_Count(iCon.getString("Prize_Available_Count"));
                        cardPrizeModel.setPrize_Won_Count(iCon.getString("Prize_Won_Count"));
                        cardPrizeModel.setTotal_Prize_Count(iCon.getString("Total_Prize_Count"));
                        allCards.add(cardPrizeModel);

                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}
