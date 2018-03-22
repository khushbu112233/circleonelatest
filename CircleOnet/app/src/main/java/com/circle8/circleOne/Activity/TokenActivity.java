package com.circle8.circleOne.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.PrefUtils;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.TimeReceiver;
import com.circle8.circleOne.Utils.Timer_Service;
import com.circle8.circleOne.databinding.TokenActivityLayoutBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by ample-arch on 2/23/2018.
 */

public class TokenActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    TokenActivityLayoutBinding tokenActivityLayoutBinding;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenActivityLayoutBinding = DataBindingUtil.setContentView(this,R.layout.token_activity_layout);

        tokenActivityLayoutBinding.imageLeft.setOnClickListener(this);
        tokenActivityLayoutBinding.imageRight.setOnClickListener(this);
        tokenActivityLayoutBinding.sliderZoom.setOnSeekBarChangeListener(this);

        tokenActivityLayoutBinding.sliderZoom.setMax(Pref.getValue(TokenActivity.this,"Rewards_Points_Remain",0));

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_VERTICAL, tokenActivityLayoutBinding.sliderZoom.getId());
        Rect thumbRect = tokenActivityLayoutBinding.sliderZoom.getSeekBarThumb().getBounds();
        p.setMargins(
                thumbRect.centerX(),0, 0, 0);
        tokenActivityLayoutBinding.txtSeekValue.setLayoutParams(p);
        tokenActivityLayoutBinding.txtSeekValue.setText(String.valueOf(Pref.getValue(TokenActivity.this,"Rewards_Points_Remain",0)) + "");
        tokenActivityLayoutBinding.txt4.setText(String.valueOf(Pref.getValue(TokenActivity.this,"Rewards_Points_Remain",0)) + "");

        prefUtils = new PrefUtils(getApplicationContext());

        TextView txt1 = (TextView) findViewById(R.id.txtDay1);
        int[] color = {Color.parseColor("#dfc977"), Color.parseColor("#ffffff")};
        float[] position = {0, 1};
        Shader.TileMode tile_mode0 = Shader.TileMode.REPEAT; // or TileMode.REPEAT;
        LinearGradient lin_grad0 = new LinearGradient(0, 0, 0, 200, color, position, tile_mode0);
        Shader shader_gradient0 = lin_grad0;
        txt1.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.txtDay2.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.txtHour1.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.txtHour2.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.txtMinute1.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.txtMinute2.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.txtSecond1.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.txtSecond2.getPaint().setShader(shader_gradient0);
        tokenActivityLayoutBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (timerState == TimerState.STOPPED) {
            prefUtils.setStartedTime((int) getNow());
            startTimer();
            timerState = TimerState.RUNNING;
        }


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

        tokenActivityLayoutBinding.imgrightDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = popupWindowsort();
                popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 0, 0);

            }
        });
    }

    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(TokenActivity.this);

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
                Pref.setValue(TokenActivity.this,"History","2");
                Intent i = new Intent(TokenActivity.this,PrizeHistoryActivity.class);
                startActivity(i);
            }
        });

        // set on item selected

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content

        return popupWindow;
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
        //Toast.makeText(this, "Countdown timer finished!", Toast.LENGTH_SHORT).show();
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
                tokenActivityLayoutBinding.txtDay1.setText(day.substring(0, 1));
                tokenActivityLayoutBinding.txtDay2.setText(day.substring(1, 2));
            } else {
                tokenActivityLayoutBinding.txtDay1.setText("0");
                tokenActivityLayoutBinding.txtDay2.setText(day);
            }
        }catch (Exception e){

        }

        try {
            if (hour.length() >= 2) {
                tokenActivityLayoutBinding.txtHour1.setText(hour.substring(0, 1));
                tokenActivityLayoutBinding.txtHour2.setText(hour.substring(1, 2));
            } else {
                tokenActivityLayoutBinding.txtHour1.setText("0");
                tokenActivityLayoutBinding.txtHour2.setText(hour);
            }
        }catch (Exception e){

        }

        try {
            if (min.length() >= 2) {
                tokenActivityLayoutBinding.txtMinute1.setText(min.substring(0, 1));
                tokenActivityLayoutBinding.txtMinute2.setText(min.substring(1, 2));
            } else {
                tokenActivityLayoutBinding.txtMinute1.setText("0");
                tokenActivityLayoutBinding.txtMinute2.setText(min);
            }
        }catch (Exception e){

        }

        try {
            if (sec.length() >= 2) {
                tokenActivityLayoutBinding.txtSecond1.setText(sec.substring(0, 1));
                tokenActivityLayoutBinding.txtSecond2.setText(sec.substring(1, 2));
            } else {
                tokenActivityLayoutBinding.txtSecond1.setText("0");
                tokenActivityLayoutBinding.txtSecond2.setText(sec);
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
    
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageLeft:
                // Do something
                tokenActivityLayoutBinding.sliderZoom.setProgress(tokenActivityLayoutBinding.sliderZoom.getProgress()-5);
                break;

            case R.id.imageRight:
                // Do something
                tokenActivityLayoutBinding.sliderZoom.setProgress(tokenActivityLayoutBinding.sliderZoom.getProgress()+5);
                break;
        }
    }

    /*public Drawable getThumb(int progress) {
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }*/

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       /* int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
        _testText.setText("" + progress);
        _testText.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
*/
       // tokenActivityLayoutBinding.sliderZoom.setThumb(getThumb(progress));

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_VERTICAL, seekBar.getId());
        Rect thumbRect = tokenActivityLayoutBinding.sliderZoom.getSeekBarThumb().getBounds();

        p.setMargins(thumbRect.centerX(),0, 0, 0);
        tokenActivityLayoutBinding.txtSeekValue.setLayoutParams(p);
        tokenActivityLayoutBinding.txtSeekValue.setText(String.valueOf(progress) + "");
        tokenActivityLayoutBinding.txt4.setText(String.valueOf(progress) + "");

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}