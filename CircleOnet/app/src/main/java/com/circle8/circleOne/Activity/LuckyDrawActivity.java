package com.circle8.circleOne.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.CardPrize;
import com.circle8.circleOne.Model.PrizeHistory;
import com.circle8.circleOne.PrefUtils;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.TimeReceiver;
import com.circle8.circleOne.Utils.Timer_Service;
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
    LoginSession session;
    public static String  UserId= "";
    int[] solutionArray = { 1, 2, 3, 4, 5, 6, 7, 8 };
    ArrayList<CardPrize> allCards = new ArrayList<>();
    public static ArrayList<PrizeHistory> prizeHistorys = new ArrayList<>();
    public static ArrayList<PrizeHistory> prizeHistorysAll = new ArrayList<>();
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

        luckyDrawLayoutBinding.includePrize.rtlHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.setValue(LuckyDrawActivity.this,"History","2");
                Intent i = new Intent(LuckyDrawActivity.this,PrizeHistoryActivity.class);
                startActivity(i);

               }
        });
        luckyDrawLayoutBinding.includePrize.rtlprize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.setValue(LuckyDrawActivity.this,"History","1");
                Intent i = new Intent(LuckyDrawActivity.this,PrizeHistoryActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        //initializing a countdown timer
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
        Toast.makeText(this, "Countdown timer finished!", Toast.LENGTH_SHORT).show();
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
                new HttpAsyncTaskRedeem().execute(Utility.BASE_URL+"RewardsGame/RedeemPointsToToken");
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);
                String message = response.getString("message");
                String success = response.getString("success");
                String userid = response.getString("userid");
                String No_Of_Tokens = response.getString("No_Of_Tokens");
                String Rewards_Points_Remain = response.getString("Rewards_Points_Remain");
                luckyDrawLayoutBinding.includePrize.txtTokenCount.setText(Rewards_Points_Remain);
                luckyDrawLayoutBinding.includePrize.txtRefreshCount.setText(No_Of_Tokens);
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
            new HttpAsyncTaskprizeHistory().execute(Utility.BASE_URL+"RewardsGame/PrizeHistory_User");

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
    private class HttpAsyncTaskprizeHistory extends AsyncTask<String, Void, String>
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
            return Utility.POST2(urls[0],jsonObject);
        }
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("response_prize_history",""+result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            new HttpAsyncTaskprizeHistoryAll().execute(Utility.BASE_URL+"RewardsGame/PrizeHistory_All");
            try
            {
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);

                String message = response.getString("message");
                String success = response.getString("success");
                String userid = response.getString("userid");
                if(success.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("prize_details");
                    prizeHistorys.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject iCon = jsonArray.getJSONObject(i);
                        PrizeHistory prizeHistoryModel = new PrizeHistory();
                        prizeHistoryModel.setPrize_ID(iCon.getString("Prize_ID"));
                        prizeHistoryModel.setPrize_Name(iCon.getString("Prize_Name"));
                        prizeHistoryModel.setPrize_Image(iCon.getString("Prize_Image"));
                        prizeHistoryModel.setResult(iCon.getString("Result"));
                        prizeHistoryModel.setPlay_Date(iCon.getString("Play_Date"));
                        prizeHistorys.add(prizeHistoryModel);

                    }


                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    private class HttpAsyncTaskprizeHistoryAll extends AsyncTask<String, Void, String>
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

            return Utility.GET(urls[0]);
        }
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("response_prize_All",""+result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                JSONObject response = new JSONObject(result);
                Log.e("response",""+response);
                    JSONArray jsonArray = response.getJSONArray("prize_details");
                    prizeHistorysAll.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject iCon = jsonArray.getJSONObject(i);
                        PrizeHistory prizeHistoryModel = new PrizeHistory();
                        prizeHistoryModel.setUserId(iCon.getString("userid"));
                        prizeHistoryModel.setPrize_ID(iCon.getString("Prize_ID"));
                        prizeHistoryModel.setPrize_Name(iCon.getString("Prize_Name"));
                        prizeHistoryModel.setPrize_Image(iCon.getString("Prize_Image"));
                        prizeHistoryModel.setResult(iCon.getString("Result"));
                        prizeHistoryModel.setPlay_Date(iCon.getString("Play_Date"));
                        prizeHistorysAll.add(prizeHistoryModel);

                    }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
