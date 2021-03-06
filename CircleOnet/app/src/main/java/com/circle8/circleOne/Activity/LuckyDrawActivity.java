package com.circle8.circleOne.Activity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.CardPrize;
import com.circle8.circleOne.Model.PrizeHistory;
import com.circle8.circleOne.PrefUtils;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.TimeReceiver;
import com.circle8.circleOne.Utils.Timer_Service;
import com.circle8.circleOne.Utils.TouchImageView;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.LuckyDrawLayoutBinding;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LuckyDrawActivity extends AppCompatActivity {
    LuckyDrawLayoutBinding luckyDrawLayoutBinding;
    ArrayList<String> stringArrayList = new ArrayList<>();
    int pos=0;
    CountDownTimer timer;
    int count=0;
    int dialogClick=0;
    LoginSession session;
    public static String  UserId= "";
    ArrayList<String> cardListClickDialog = new ArrayList<>();
    int[] solutionArray = { 1, 2, 3, 4, 5, 6, 7, 8 };
    ArrayList<CardPrize> allCards = new ArrayList<>();

    int id = 0;
    ArrayList<Integer> prizeIdList= new ArrayList<>();
    String[] pointArray = {"CircleOne Points","Apple ipad pro","NFC Name Card","CircleOne Points","CircleOne Points","Amazon Kindle","Earphone","CircleOne Points"};
    String date_time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    // EditText et_hours;
    private PrefUtils prefUtils;
    private CountDownTimer countDownTimer;
    private int timeToStart;
    private TimerState timerState;
    private static final int MAX_TIME = 86410;
    private PopupWindow popupWindow;
    float x,y,x1,y1,x2,y2,x3,y3,x4,y4,x5,y5,x6,y6,x7,y7,x8,y8,xTemp,yTemp;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        luckyDrawLayoutBinding = DataBindingUtil.setContentView(this,R.layout.lucky_draw_layout);

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        x1 = luckyDrawLayoutBinding.easyFlipView1.getX();
        y1 = luckyDrawLayoutBinding.easyFlipView1.getY();

        x2 = luckyDrawLayoutBinding.easyFlipView2.getX();
        y2 = luckyDrawLayoutBinding.easyFlipView2.getY();

        x3 = luckyDrawLayoutBinding.easyFlipView3.getX();
        y3 = luckyDrawLayoutBinding.easyFlipView3.getY();

        x4 = luckyDrawLayoutBinding.easyFlipView4.getX();
        y4 = luckyDrawLayoutBinding.easyFlipView4.getY();

        x5 = luckyDrawLayoutBinding.easyFlipView5.getX();
        y5 = luckyDrawLayoutBinding.easyFlipView5.getY();

        x6 = luckyDrawLayoutBinding.easyFlipView6.getX();
        y6 = luckyDrawLayoutBinding.easyFlipView6.getY();

        x7 = luckyDrawLayoutBinding.easyFlipView7.getX();
        y7 = luckyDrawLayoutBinding.easyFlipView7.getY();

        x8 = luckyDrawLayoutBinding.easyFlipView8.getX();
        y8 = luckyDrawLayoutBinding.easyFlipView8.getY();

        luckyDrawLayoutBinding.includePrize.rtlRedeem.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                luckyDrawLayoutBinding.includePrize.rtlRedeem.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int[] locations = new int[2];
                luckyDrawLayoutBinding.includePrize.rtlRedeem.getLocationOnScreen(locations);
                x = locations[0];
                y = locations[1];

            }
        });

        UserId = user.get(LoginSession.KEY_USERID);

        luckyDrawLayoutBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new HttpAsyncTask().execute(Utility.BASE_URL+"RewardsGame/Refresh");

      //  new HttpAsyncTaskWininingPrize().execute(Utility.BASE_URL+"RewardsGame/WinningPrize");
        stringArrayList.add("test fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test1 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test2 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test3 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test4 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        stringArrayList.add("test5 fsdf sdfs dsfsd sdff sdfsdf sdfs sdfsdfs sdfsfs sdfsdfdsf dsfdsfsdf sdfsdfs");
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.shuffle);
        prefUtils = new PrefUtils(getApplicationContext());

        //  manageBlinkEffect();

        TextView txt1 = (TextView) findViewById(R.id.txtDay1);
        int[] color = {Color.parseColor("#dfc977"), Color.parseColor("#ffffff")};
        float[] position = {0, 1};
        Shader.TileMode tile_mode0 = Shader.TileMode.REPEAT; // or TileMode.REPEAT;
        LinearGradient lin_grad0 = new LinearGradient(0, 0, 0, 200, color, position, tile_mode0);
        Shader shader_gradient0 = lin_grad0;
        txt1.getPaint().setShader(shader_gradient0);
        luckyDrawLayoutBinding.txtDay2.getPaint().setShader(shader_gradient0);
        luckyDrawLayoutBinding.txtHour1.getPaint().setShader(shader_gradient0);
        luckyDrawLayoutBinding.txtHour2.getPaint().setShader(shader_gradient0);
        luckyDrawLayoutBinding.txtMinute1.getPaint().setShader(shader_gradient0);
        luckyDrawLayoutBinding.txtMinute2.getPaint().setShader(shader_gradient0);
        luckyDrawLayoutBinding.txtSecond1.getPaint().setShader(shader_gradient0);
        luckyDrawLayoutBinding.txtSecond2.getPaint().setShader(shader_gradient0);

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



        luckyDrawLayoutBinding.llTemp.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                luckyDrawLayoutBinding.llTemp.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int[] locations = new int[2];
                luckyDrawLayoutBinding.llTemp.getLocationOnScreen(locations);
                xTemp = locations[0];
                yTemp = locations[1];

            }
        });

        luckyDrawLayoutBinding.includePrize.rtlRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LuckyDrawActivity.this,RedeemActivity.class);
                startActivity(i);

            }
        });
        luckyDrawLayoutBinding.includePrize.rtlprize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LuckyDrawActivity.this,PrizeActivity.class);
                startActivity(i);

            }
        });
        luckyDrawLayoutBinding.includePrize.rtlToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LuckyDrawActivity.this,TokenActivity.class);
                startActivity(i);

            }
        });


        luckyDrawLayoutBinding.includePrize.rtlRefresh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                count = 0;
                mp.start();

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

/*
                luckyDrawLayoutBinding.rtlTemp.setVisibility(View.VISIBLE);
                luckyDrawLayoutBinding.llTemp.setVisibility(View.GONE);
                luckyDrawLayoutBinding.rl1.setVisibility(View.GONE);
                luckyDrawLayoutBinding.rl2.setVisibility(View.GONE);*/
                luckyDrawLayoutBinding.rl1.setVisibility(View.GONE);
                luckyDrawLayoutBinding.rl2.setVisibility(View.GONE);
                luckyDrawLayoutBinding.llTemp.setBackgroundResource(R.drawable.animation_list_filling);
/*

                luckyDrawLayoutBinding.easyFlipView1.setVisibility(View.GONE);
                luckyDrawLayoutBinding.easyFlipView2.setVisibility(View.GONE);
                luckyDrawLayoutBinding.easyFlipView3.setVisibility(View.GONE);
                luckyDrawLayoutBinding.easyFlipView4.setVisibility(View.GONE);
                luckyDrawLayoutBinding.easyFlipView5.setVisibility(View.GONE);
                luckyDrawLayoutBinding.easyFlipView6.setVisibility(View.GONE);
                luckyDrawLayoutBinding.easyFlipView7.setVisibility(View.GONE);
                luckyDrawLayoutBinding.easyFlipView8.setVisibility(View.GONE);
*/

                ((AnimationDrawable) luckyDrawLayoutBinding.llTemp.getBackground()).start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        luckyDrawLayoutBinding.rl1.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.rl2.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) luckyDrawLayoutBinding.llTemp.getBackground()).stop();
                        luckyDrawLayoutBinding.llTemp.setBackgroundResource(0);


                        /*
                        luckyDrawLayoutBinding.easyFlipView1.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.easyFlipView2.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.easyFlipView3.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.easyFlipView4.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.easyFlipView5.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.easyFlipView6.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.easyFlipView7.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.easyFlipView8.setVisibility(View.VISIBLE);
*/
                    }
                }, 750);

                cardValueSet();
                /*luckyDrawLayoutBinding.easyFlipViewr1.setX(x1);
                luckyDrawLayoutBinding.easyFlipViewr1.setY(y1);
                luckyDrawLayoutBinding.easyFlipViewr2.setX(x2);
                luckyDrawLayoutBinding.easyFlipViewr2.setY(y2);
                luckyDrawLayoutBinding.easyFlipViewr3.setX(x3);
                luckyDrawLayoutBinding.easyFlipViewr3.setY(y3);
                luckyDrawLayoutBinding.easyFlipViewr4.setX(x4);
                luckyDrawLayoutBinding.easyFlipViewr4.setY(y4);
                luckyDrawLayoutBinding.easyFlipViewr5.setX(x5);
                luckyDrawLayoutBinding.easyFlipViewr5.setY(y5);
                luckyDrawLayoutBinding.easyFlipViewr6.setX(x6);
                luckyDrawLayoutBinding.easyFlipViewr6.setY(y6);
                luckyDrawLayoutBinding.easyFlipViewr7.setX(x7);
                luckyDrawLayoutBinding.easyFlipViewr7.setY(y7);
                luckyDrawLayoutBinding.easyFlipViewr8.setX(x8);
                luckyDrawLayoutBinding.easyFlipViewr8.setY(y8);*/

/*

                luckyDrawLayoutBinding.easyFlipViewr1.animate()
                        .translationX((width / 2)-100)
                        .translationY((yTemp/2)-25)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr1.animate()
                                        .translationX((width / 2)-(100+(width/4)))
                                        .translationY((yTemp/2)-25)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }
                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr1.animate()
                                                        .translationX((width / 2)-100)
                                                        .translationY((yTemp/2)-25)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {
                                                            }
                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr1.animate()
                                                                        .translationX(x1)
                                                                        .translationY(y1)
                                                                        .setDuration(1000);
                                                            }
                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {
                                                            }
                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {
                                                            }
                                                        })
                                                        .setDuration(1000);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);



                luckyDrawLayoutBinding.easyFlipViewr2.animate()
                        .translationX((width / 2)-(100+(width/4)))
                        .translationY((yTemp/2)-25)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr2.animate()
                                        .translationX((width / 2)-100)
                                        .translationY((yTemp/2)-25)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr2.animate()
                                                        .translationX((width / 2)-(100+(width/4)))
                                                        .translationY((yTemp/2)-25)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr2.animate()
                                                                        .translationX(x2)
                                                                        .translationY(y2)
                                                                        .setDuration(1000);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {

                                                            }
                                                        })
                                                        .setDuration(1000);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);


                luckyDrawLayoutBinding.easyFlipViewr3.animate()
                        .translationX((width / 2)-(100+(width/2)))
                        .translationY((yTemp/2)-25)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr3.animate()
                                        .translationX((width / 2)-(100+(width)))
                                        .translationY((yTemp/2)-25)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr3.animate()
                                                        .translationX((width / 2)-(100+(width/2)))
                                                        .translationY((yTemp/2)-25)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr3.animate()
                                                                        .translationX(x3)
                                                                        .translationY(y3)
                                                                        .setDuration(1000);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {

                                                            }
                                                        })
                                                        .setDuration(1000);

                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);

                luckyDrawLayoutBinding.easyFlipViewr4.animate()
                        .translationX((width / 2)-(100+(width)))
                        .translationY((yTemp/2)-25)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr4.animate()
                                        .translationX((width / 2)-(100+(width/2)))
                                        .translationY((yTemp/2)-25)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }
                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr4.animate()
                                                        .translationX((width / 2)-(100+(width)))
                                                        .translationY((yTemp/2)-25)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr4.animate()
                                                                        .translationX(x4)
                                                                        .translationY(y4)
                                                                        .setDuration(1000);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {

                                                            }
                                                        })
                                                        .setDuration(1000);
                                            }
                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }
                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);

                luckyDrawLayoutBinding.easyFlipViewr5.animate()
                        .translationX((width / 2)-100)
                        .translationY((yTemp/2)-250)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr5.animate()
                                        .translationX((width / 2)-(100+(width/4)))
                                        .translationY((yTemp/2)-250)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr5.animate()
                                                        .translationX((width / 2)-100)
                                                        .translationY((yTemp/2)-250)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr5.animate()
                                                                        .translationX(x5)
                                                                        .translationY(y5)
                                                                        .setDuration(1000);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {

                                                            }
                                                        })
                                                        .setDuration(1000);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);

                luckyDrawLayoutBinding.easyFlipViewr6.animate()
                        .translationX((width / 2)-(100+(width/4)))
                        .translationY((yTemp/2)-250)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr6.animate()
                                        .translationX((width / 2)-100)
                                        .translationY((yTemp/2)-250)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr6.animate()
                                                        .translationX((width / 2)-(100+(width/4)))
                                                        .translationY((yTemp/2)-250)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr6.animate()
                                                                        .translationX(x6)
                                                                        .translationY(y6)
                                                                        .setDuration(1000);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {

                                                            }
                                                        })
                                                        .setDuration(1000);

                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);

                luckyDrawLayoutBinding.easyFlipViewr7.animate()
                        .translationX((width / 2)-(100+(width/2)))
                        .translationY((yTemp/2)-250)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr7.animate()
                                        .translationX((width / 2)-(100+(width)))
                                        .translationY((yTemp/2)-250)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr7.animate()
                                                        .translationX((width / 2)-(100+(width/2)))
                                                        .translationY((yTemp/2)-250)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr7.animate()
                                                                        .translationX(x7)
                                                                        .translationY(y7)
                                                                        .setDuration(1000);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {

                                                            }
                                                        })
                                                        .setDuration(1000);

                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);

                luckyDrawLayoutBinding.easyFlipViewr8.animate()
                        .translationX((width / 2)-(100+(width)))
                        .translationY((yTemp/2)-250)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                luckyDrawLayoutBinding.easyFlipViewr8.animate()
                                        .translationX((width / 2)-(100+(width/2)))
                                        .translationY((yTemp/2)-250)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                luckyDrawLayoutBinding.easyFlipViewr8.animate()
                                                        .translationX((width / 2)-(100+(width)))
                                                        .translationY((yTemp/2)-250)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animator) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animator) {
                                                                luckyDrawLayoutBinding.easyFlipViewr8.animate()
                                                                        .translationX(x8)
                                                                        .translationY(y8)
                                                                        .setDuration(1000);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animator) {


                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animator) {

                                                            }
                                                        })
                                                        .setDuration(1000);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .setDuration(1000);

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .setDuration(1000);


                luckyDrawLayoutBinding.easyFlipViewr1.setX(x1);
                luckyDrawLayoutBinding.easyFlipViewr1.setY(y1);
                luckyDrawLayoutBinding.easyFlipViewr2.setX(x2);
                luckyDrawLayoutBinding.easyFlipViewr2.setY(y2);
                luckyDrawLayoutBinding.easyFlipViewr3.setX(x3);
                luckyDrawLayoutBinding.easyFlipViewr3.setY(y3);
                luckyDrawLayoutBinding.easyFlipViewr4.setX(x4);
                luckyDrawLayoutBinding.easyFlipViewr4.setY(y4);
                luckyDrawLayoutBinding.easyFlipViewr5.setX(x5);
                luckyDrawLayoutBinding.easyFlipViewr5.setY(y5);
                luckyDrawLayoutBinding.easyFlipViewr6.setX(x6);
                luckyDrawLayoutBinding.easyFlipViewr6.setY(y6);
                luckyDrawLayoutBinding.easyFlipViewr7.setX(x7);
                luckyDrawLayoutBinding.easyFlipViewr7.setY(y7);
                luckyDrawLayoutBinding.easyFlipViewr8.setX(x8);
                luckyDrawLayoutBinding.easyFlipViewr8.setY(y8);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        luckyDrawLayoutBinding.rtlTemp.setVisibility(View.GONE);
                        luckyDrawLayoutBinding.llTemp.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.rl1.setVisibility(View.VISIBLE);
                        luckyDrawLayoutBinding.rl2.setVisibility(View.VISIBLE);

                    }
                }, 1000);
*/
            }
        });
        luckyDrawLayoutBinding.imgrightDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = popupWindowsort();
                popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 0, 0);

            }
        });
        luckyDrawLayoutBinding.imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos--;
                if(pos==0)
                {
                    luckyDrawLayoutBinding.imgLeft.setClickable(false);
                }else if(pos==stringArrayList.size()-1)
                {
                    luckyDrawLayoutBinding.imgRight.setClickable(true);
                }else
                {
                    luckyDrawLayoutBinding.imgLeft.setClickable(true);
                    luckyDrawLayoutBinding.imgRight.setClickable(true);
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
                    luckyDrawLayoutBinding.imgRight.setClickable(false);
                }else if(pos==1)
                {
                    luckyDrawLayoutBinding.imgLeft.setClickable(true);
                }else
                {
                    luckyDrawLayoutBinding.imgLeft.setClickable(true);
                    luckyDrawLayoutBinding.imgRight.setClickable(true);
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

                if(luckyDrawLayoutBinding.easyFlipView1.isBackSide()) {
                    dialogClick++;
                    cardListClickDialog.add(allCards.get(0).getPrize_ID());
                    dialog(allCards,0);
                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(0).getPrize_ID()));
                }
            }
        });
        luckyDrawLayoutBinding.easyFlipView2.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);

                if(luckyDrawLayoutBinding.easyFlipView2.isBackSide()) {
                    dialogClick++;
                    cardListClickDialog.add(allCards.get(1).getPrize_ID());
                    dialog(allCards,1);

                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(1).getPrize_ID()));
                }
            }
        });
        luckyDrawLayoutBinding.easyFlipView3.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);

                if(luckyDrawLayoutBinding.easyFlipView3.isBackSide()) {
                    dialogClick++;
                    cardListClickDialog.add(allCards.get(2).getPrize_ID());
                    dialog(allCards,2);

                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(2).getPrize_ID()));
                }
            }
        });
        luckyDrawLayoutBinding.easyFlipView4.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);

                if(luckyDrawLayoutBinding.easyFlipView4.isBackSide()) {
                    cardListClickDialog.add(allCards.get(3).getPrize_ID());
                    dialogClick++;
                    dialog(allCards,3);
                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(3).getPrize_ID()));
                }
            }
        });
        luckyDrawLayoutBinding.easyFlipView5.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);

                if(luckyDrawLayoutBinding.easyFlipView5.isBackSide()) {
                    cardListClickDialog.add(allCards.get(4).getPrize_ID());
                    dialogClick++;
                    dialog(allCards,4);

                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(4).getPrize_ID()));
                }
            }
        });
        luckyDrawLayoutBinding.easyFlipView6.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);

                if(luckyDrawLayoutBinding.easyFlipView6.isBackSide()) {
                    cardListClickDialog.add(allCards.get(5).getPrize_ID());
                    dialogClick++;
                    dialog(allCards,5);

                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(5).getPrize_ID()));
                }
            }
        });
        luckyDrawLayoutBinding.easyFlipView7.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {

                count++;
                countMaintain(count);

                if(luckyDrawLayoutBinding.easyFlipView7.isBackSide()) {
                    cardListClickDialog.add(allCards.get(6).getPrize_ID());
                    dialogClick++;
                    dialog(allCards,6);

                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(6).getPrize_ID()));
                }
            }
        });
        luckyDrawLayoutBinding.easyFlipView8.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                count++;
                countMaintain(count);

                if(luckyDrawLayoutBinding.easyFlipView8.isBackSide())
                {
                    cardListClickDialog.add(allCards.get(7).getPrize_ID());
                    dialogClick++;
                    dialog(allCards,7);

                }else {
                    flipView.setClickable(false);
                    flipView.setEnabled(false);
                    flipView.setFlipEnabled(false);
                    flipView.setFlipOnTouch(false);
                    prizeIdList.add(Integer.parseInt(allCards.get(7).getPrize_ID()));
                }
            }
        });

    }

    private void dialog(ArrayList<CardPrize> strlist,int pos) {
        final Dialog dialog=new Dialog(LuckyDrawActivity.this);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.layout_full_image);
        TouchImageView bmImage = (TouchImageView) dialog.findViewById(R.id.img_receipt);
        TextView txtValue = (TextView)dialog.findViewById(R.id.txtValue);
        final ImageView imgCenter = (ImageView)dialog.findViewById(R.id.imgCenter);
        TextView txtpoint = (TextView)dialog.findViewById(R.id.txtpoint);
        TextView txtTop =  (TextView)dialog.findViewById(R.id.txtTop);
        TextView txtBottom =  (TextView)dialog.findViewById(R.id.txtBottom);
        bmImage.setImageResource(R.drawable.ic_gold_bg);
        txtValue.setVisibility(View.GONE);
        txtpoint.setText(strlist.get(pos).getPrize_Name());
        txtTop.setText(strlist.get(pos).getPrize_Name());
        txtBottom.setText(strlist.get(pos).getPrize_Name());
        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + strlist.get(pos).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(imgCenter) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        imgCenter.setImageBitmap(drawable);
                    }
                });
        final ImageView button=(ImageView)dialog.findViewById(R.id.imgClose);
        dialog.setCancelable(true);
        dialog.show();
        //  animationShrink = AnimationUtils.loadAnimation(this, R.anim.animation);
        //luckyDrawLayoutBinding.includePrize.rtlRedeem.startAnimation(animationShrink);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View decorView = dialog
                        .getWindow()
                        .getDecorView();

                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(decorView,
                        PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f),
                        PropertyValuesHolder.ofFloat("pivotX", 1.0f, x+80),
                        PropertyValuesHolder.ofFloat("pivotY", 1.0f, y),
                        PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f));
                scaleDown.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(dialogClick==2)
                        {
                            Log.e("dialogClick",""+dialogClick+"   "+cardListClickDialog);
                            new HttpAsyncTaskGetPushCards().execute(Utility.BASE_URL+"RewardsGame/Push_Cards");
                        }
                        dialog.dismiss();
                    }
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                scaleDown.setDuration(2000);
                scaleDown.start();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //initializing a countdown timer
        if (timerState == TimerState.STOPPED) {
            prefUtils.setStartedTime((int) getNow());
            startTimer();
            timerState = TimerState.RUNNING;
        }


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
                        luckyDrawLayoutBinding.imgLeft.setClickable(false);
                        luckyDrawLayoutBinding.imgRight.setClickable(true);
                    }else if(pos==1)
                    {
                        luckyDrawLayoutBinding.imgLeft.setClickable(true);
                        luckyDrawLayoutBinding.imgRight.setClickable(true);
                    }else if(pos==stringArrayList.size()-1)
                    {
                        luckyDrawLayoutBinding.imgLeft.setClickable(true);
                        luckyDrawLayoutBinding.imgRight.setClickable(false);
                    }

                    luckyDrawLayoutBinding.txtDesc.setText(stringArrayList.get(pos));
                    timer.start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();




        int int_hours = 1;

        if (int_hours<=24) {


            //et_hours.setEnabled(false);
            // btn_start.setEnabled(false);
            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            date_time = simpleDateFormat.format(calendar.getTime());

            Intent intent_service = new Intent(getApplicationContext(), Timer_Service.class);
            startService(intent_service);
        }else {
            Toast.makeText(getApplicationContext(),"Please select the value below 24 hours",Toast.LENGTH_SHORT).show();
        }
        initTimer();
        updatingUI();
        removeAlarmManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerState == TimerState.RUNNING) {
            countDownTimer.cancel();
            setAlarmManager();
        }
    }

    private long getNow() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.getTimeInMillis() / 1000;
    }

    private void initTimer() {
        long startTime = prefUtils.getStartedTime();
        if (startTime > 0) {
            timeToStart = (int) (MAX_TIME - (getNow() - startTime));
            if (timeToStart <= 0) {
                // TIMER EXPIRED
                timeToStart = MAX_TIME;
                timerState = TimerState.STOPPED;
                onTimerFinish();
            } else {
                startTimer();
                timerState = TimerState.RUNNING;
            }
        } else {
            timeToStart = MAX_TIME;
            timerState = TimerState.STOPPED;
        }
    }

    private void onTimerFinish() {
       // Toast.makeText(this, "Countdown timer finished!", Toast.LENGTH_SHORT).show();
        prefUtils.setStartedTime(0);
        timeToStart = MAX_TIME;
        updatingUI();
    }

    private void updatingUI() {
        if (timerState == TimerState.RUNNING) {
           /* btnStart.setEnabled(false);
            noticeText.setText("Countdown Timer is running...");*/
        } else {
            /*btnStart.setEnabled(true);
            noticeText.setText("Countdown Timer stopped!");
*/
            if (timerState == TimerState.STOPPED) {
                prefUtils.setStartedTime((int) getNow());
                startTimer();
                timerState = TimerState.RUNNING;
            }
        }


        int days = (int) TimeUnit.SECONDS.toDays(timeToStart);
        long hours = TimeUnit.SECONDS.toHours(timeToStart) - (days *24);
        long minute = TimeUnit.SECONDS.toMinutes(timeToStart) - (TimeUnit.SECONDS.toHours(timeToStart)* 60);
        long second = TimeUnit.SECONDS.toSeconds(timeToStart) - (TimeUnit.SECONDS.toMinutes(timeToStart) *60);

        //timerText.setText(String.valueOf(day+ ":" + hours + ":" + minute + ":" + second));
        String day = String.valueOf(days);
        String hour = hours +"";
        String min = minute +"";
        String sec = second +"";
        try {
            if (day.length() >= 2) {
                luckyDrawLayoutBinding.txtDay1.setText(day.substring(0, 1));
                luckyDrawLayoutBinding.txtDay2.setText(day.substring(1, 2));
            } else {
                luckyDrawLayoutBinding.txtDay1.setText("0");
                luckyDrawLayoutBinding.txtDay2.setText(day);
            }
        }catch (Exception e){

        }

        try {
            if (hour.length() >= 2) {
                luckyDrawLayoutBinding.txtHour1.setText(hour.substring(0, 1));
                luckyDrawLayoutBinding.txtHour2.setText(hour.substring(1, 2));
            } else {
                luckyDrawLayoutBinding.txtHour1.setText("0");
                luckyDrawLayoutBinding.txtHour2.setText(hour);
            }
        }catch (Exception e){

        }

        try {
            if (min.length() >= 2) {
                luckyDrawLayoutBinding.txtMinute1.setText(min.substring(0, 1));
                luckyDrawLayoutBinding.txtMinute2.setText(min.substring(1, 2));
            } else {
                luckyDrawLayoutBinding.txtMinute1.setText("0");
                luckyDrawLayoutBinding.txtMinute2.setText(min);
            }
        }catch (Exception e){

        }

        try {
            if (sec.length() >= 2) {
                luckyDrawLayoutBinding.txtSecond1.setText(sec.substring(0, 1));
                luckyDrawLayoutBinding.txtSecond2.setText(sec.substring(1, 2));
            } else {
                luckyDrawLayoutBinding.txtSecond1.setText("0");
                luckyDrawLayoutBinding.txtSecond2.setText(sec);
            }
        }catch (Exception e){

        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeToStart * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeToStart -= 1;
                updatingUI();
            }

            @Override
            public void onFinish() {
                timerState = TimerState.STOPPED;
                onTimerFinish();
                updatingUI();
            }
        }.start();
    }

    public void setAlarmManager() {
        int wakeUpTime = (prefUtils.getStartedTime() + MAX_TIME) * 1000;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            am.setAlarmClock(new AlarmManager.AlarmClockInfo(wakeUpTime, sender), sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, wakeUpTime, sender);
        }
    }

    public void removeAlarmManager() {
        Intent intent = new Intent(this, TimeReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    private enum TimerState {
        STOPPED,
        RUNNING
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
    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(LuckyDrawActivity.this);

        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // setContentView(inflator.inflate(layoutResID, null));

        View view1 = inflator.inflate(R.layout.layout_lucky_menu, null);
        popupWindow.setContentView(view1);
        popupWindow.setOutsideTouchable(true);

        RelativeLayout rltHistory = view1.findViewById(R.id.rltHistory);
        RelativeLayout rltContactUs = view1.findViewById(R.id.rltContactUs);
        RelativeLayout rltHelp = view1.findViewById(R.id.rltHelp);

        ImageView imgDismiss = view1.findViewById(R.id.imgMenu);

        // set our adapter and pass our pop up window contents

        imgDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });


        // set on item selected

        rltHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.setValue(LuckyDrawActivity.this,"History","2");
                Intent i = new Intent(LuckyDrawActivity.this,PrizeHistoryActivity.class);
                startActivity(i);
            }
        });
        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content

        return popupWindow;
    }

    public void countMaintain(int count)
    {
        if(count>=2) {
            luckyDrawLayoutBinding.easyFlipView1.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView2.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView3.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView4.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView5.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView6.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView7.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView8.setEnabled(false);
            luckyDrawLayoutBinding.easyFlipView1.setClickable(false);
            luckyDrawLayoutBinding.easyFlipView2.setClickable(false);
            luckyDrawLayoutBinding.easyFlipView3.setClickable(false);
            luckyDrawLayoutBinding.easyFlipView4.setClickable(false);
            luckyDrawLayoutBinding.easyFlipView5.setClickable(false);
            luckyDrawLayoutBinding.easyFlipView6.setClickable(false);
            luckyDrawLayoutBinding.easyFlipView7.setClickable(false);
            luckyDrawLayoutBinding.easyFlipView8.setClickable(false);
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
            Log.e("responseCardDetails",""+result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                new HttpAsyncTaskRedeem().execute(Utility.BASE_URL+"RewardsGame/RedeemPointsToToken");
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);
                String message = response.getString("message");
                String success = response.getString("success");
                String userid = response.getString("userid");
                String No_Of_Tokens = response.getString("No_Of_Tokens");
                String Rewards_Points_Remain = response.getString("Rewards_Points_Remain");
                luckyDrawLayoutBinding.includePrize.txtTokenCount.setText(210/5+"");
                Pref.setValue(LuckyDrawActivity.this,"Rewards_Points_Remain",(210/5));
                luckyDrawLayoutBinding.includePrize.txtRefreshCount.setText(No_Of_Tokens);
                if(success.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("cards_8");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject iCon = jsonArray.getJSONObject(i);
                        CardPrize cardPrizeModel = new CardPrize();
                        cardPrizeModel.setPrize_ID(iCon.getString("Card_ID"));
                        cardPrizeModel.setPrize_Name(iCon.getString("Card_Name"));
                        cardPrizeModel.setPrize_Image(iCon.getString("Card_Image"));
                       /* cardPrizeModel.setPrize_Available_Count(iCon.getString("Prize_Available_Count"));
                        cardPrizeModel.setPrize_Won_Count(iCon.getString("Prize_Won_Count"));
                        cardPrizeModel.setTotal_Prize_Count(iCon.getString("Total_Prize_Count"));
                       */
                       allCards.add(cardPrizeModel);

                    }
                    cardValueSet();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void cardValueSet()
    {
        luckyDrawLayoutBinding.txtpoint1.setText(allCards.get(0).getPrize_Name());
        luckyDrawLayoutBinding.txtpoint2.setText(allCards.get(1).getPrize_Name());
        luckyDrawLayoutBinding.txtpoint3.setText(allCards.get(2).getPrize_Name());
        luckyDrawLayoutBinding.txtpoint4.setText(allCards.get(3).getPrize_Name());
        luckyDrawLayoutBinding.txtpoint5.setText(allCards.get(4).getPrize_Name());
        luckyDrawLayoutBinding.txtpoint6.setText(allCards.get(5).getPrize_Name());
        luckyDrawLayoutBinding.txtpoint7.setText(allCards.get(6).getPrize_Name());
        luckyDrawLayoutBinding.txtpoint8.setText(allCards.get(7).getPrize_Name());

        luckyDrawLayoutBinding.txt1.setVisibility(View.GONE);
        luckyDrawLayoutBinding.txt2.setVisibility(View.GONE);
        luckyDrawLayoutBinding.txt3.setVisibility(View.GONE);
        luckyDrawLayoutBinding.txt4.setVisibility(View.GONE);
        luckyDrawLayoutBinding.txt5.setVisibility(View.GONE);
        luckyDrawLayoutBinding.txt6.setVisibility(View.GONE);
        luckyDrawLayoutBinding.txt7.setVisibility(View.GONE);
        luckyDrawLayoutBinding.txt8.setVisibility(View.GONE);


        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(0).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img1) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img1.setImageBitmap(drawable);
                    }
                });

        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(1).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img2) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img2.setImageBitmap(drawable);
                    }
                });

        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(2).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img3) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img3.setImageBitmap(drawable);
                    }
                });

        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(3).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img4) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img4.setImageBitmap(drawable);
                    }
                });

        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(4).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img5) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img5.setImageBitmap(drawable);
                    }
                });

        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(5).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img6) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img6.setImageBitmap(drawable);
                    }
                });

        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(6).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img7) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img7.setImageBitmap(drawable);
                    }
                });

        Glide.with(LuckyDrawActivity.this).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + allCards.get(7).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(luckyDrawLayoutBinding.img8) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        luckyDrawLayoutBinding.img8.setImageBitmap(drawable);
                    }
                });
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
            //new HttpAsyncTaskprizeHistory().execute(Utility.BASE_URL+"RewardsGame/PrizeHistory_User");

            try
            {
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);
                String message = response.getString("message");
                String success = response.getString("success");
                String userid = response.getString("userid");
                String No_Of_Tokens = response.getString("No_Of_Tokens");
                String Rewards_Points_Remain = response.getString("Rewards_Points_Remain");
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
    private class HttpAsyncTaskRedeem extends AsyncTask<String, Void, String>
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
            Log.e("response_redeem",""+result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

/*
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
            }*/
        }
    }
    private class HttpAsyncTaskGetPushCards extends AsyncTask<String, Void, String>
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
                jsonObject.accumulate("Card_ID",cardListClickDialog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Utility.POST2(urls[0],jsonObject);
        }
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("response_prize_history",""+result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);

                String message = response.getString("message");
                String success = response.getString("success");
                String userid = response.getString("userid");
                /*if(success.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("prize_details");
                    prizeHistorys.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject iCon = jsonArray.getJSONObject(i);
                        PrizeHistory prizeHistoryModel = new PrizeHistory();
                        prizeHistoryModel.setPrize_ID(iCon.getString("Prize_ID"));
                        prizeHistoryModel.setPrize_Name(iCon.getString("Prize_Name"));
                        prizeHistoryModel.setPrize_Image(iCon.getString("Prize_Image"));
                       // prizeHistoryModel.setResult(iCon.getString("Result"));
                       // prizeHistoryModel.setPlay_Date(iCon.getString("Play_Date"));
                        prizeHistorys.add(prizeHistoryModel);

                    }


                }*/
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}
