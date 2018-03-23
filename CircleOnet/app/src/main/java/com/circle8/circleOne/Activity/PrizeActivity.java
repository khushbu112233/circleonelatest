package com.circle8.circleOne.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.PrizeAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.Prize;
import com.circle8.circleOne.PrefUtils;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.TimeReceiver;
import com.circle8.circleOne.Utils.Timer_Service;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.PrizeMainLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by ample-arch on 3/22/2018.
 */

public class PrizeActivity extends AppCompatActivity {
    PrizeMainLayoutBinding prizeMainLayoutBinding;
    PrizeAdapter prizeAdapter;
    public static String  UserId= "";
    LoginSession session;
    ArrayList<Prize> prizeList = new ArrayList<>();

    String date_time;
    Calendar calendar;
    private static final int MAX_TIME = 86410;
    SimpleDateFormat simpleDateFormat;
    // EditText et_hours;
    private PrefUtils prefUtils;
    private CountDownTimer countDownTimer;
    private int timeToStart;
    private TimerState timerState;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prizeMainLayoutBinding = DataBindingUtil.setContentView(this, R.layout.prize_main_layout);
        prizeAdapter = new PrizeAdapter(PrizeActivity.this,prizeList);
        prizeMainLayoutBinding.lstPrize.setAdapter(prizeAdapter);
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        prefUtils = new PrefUtils(getApplicationContext());

        new HttpAsyncTaskprizeHistory().execute(Utility.BASE_URL + "RewardsGame/PrizeDistribution");
        TextView txt1 = (TextView) findViewById(R.id.txtDay1);
        int[] color = {Color.parseColor("#dfc977"), Color.parseColor("#ffffff")};
        float[] position = {0, 1};
        Shader.TileMode tile_mode0 = Shader.TileMode.REPEAT; // or TileMode.REPEAT;
        LinearGradient lin_grad0 = new LinearGradient(0, 0, 0, 200, color, position, tile_mode0);
        Shader shader_gradient0 = lin_grad0;
        txt1.getPaint().setShader(shader_gradient0);
        prizeMainLayoutBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        prizeMainLayoutBinding.imgrightDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = popupWindowsort();
                popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 0, 0);

            }
        });
        prizeMainLayoutBinding.txtDay2.getPaint().setShader(shader_gradient0);
        prizeMainLayoutBinding.txtHour1.getPaint().setShader(shader_gradient0);
        prizeMainLayoutBinding.txtHour2.getPaint().setShader(shader_gradient0);
        prizeMainLayoutBinding.txtMinute1.getPaint().setShader(shader_gradient0);
        prizeMainLayoutBinding.txtMinute2.getPaint().setShader(shader_gradient0);
        prizeMainLayoutBinding.txtSecond1.getPaint().setShader(shader_gradient0);
        prizeMainLayoutBinding.txtSecond2.getPaint().setShader(shader_gradient0);

        if (timerState == TimerState.STOPPED) {
            prefUtils.setStartedTime((int) getNow());
            startTimer();
            timerState = TimerState.RUNNING;
        }


        int int_hours = 1;

        if (int_hours <= 24) {


            //et_hours.setEnabled(false);
            // btn_start.setEnabled(false);


            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            date_time = simpleDateFormat.format(calendar.getTime());

            Intent intent_service = new Intent(getApplicationContext(), Timer_Service.class);
            startService(intent_service);
        } else {
            Toast.makeText(getApplicationContext(), "Please select the value below 24 hours", Toast.LENGTH_SHORT).show();
        }

    }

    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(PrizeActivity.this);

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
                Pref.setValue(PrizeActivity.this, "History", "2");
                Intent i = new Intent(PrizeActivity.this, PrizeHistoryActivity.class);
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
        long hours = TimeUnit.SECONDS.toHours(timeToStart) - (days * 24);
        long minute = TimeUnit.SECONDS.toMinutes(timeToStart) - (TimeUnit.SECONDS.toHours(timeToStart) * 60);
        long second = TimeUnit.SECONDS.toSeconds(timeToStart) - (TimeUnit.SECONDS.toMinutes(timeToStart) * 60);

        //timerText.setText(String.valueOf(day+ ":" + hours + ":" + minute + ":" + second));
        String day = String.valueOf(days);
        String hour = hours + "";
        String min = minute + "";
        String sec = second + "";
        try {
            if (day.length() >= 2) {
                prizeMainLayoutBinding.txtDay1.setText(day.substring(0, 1));
                prizeMainLayoutBinding.txtDay2.setText(day.substring(1, 2));
            } else {
                prizeMainLayoutBinding.txtDay1.setText("0");
                prizeMainLayoutBinding.txtDay2.setText(day);
            }
        } catch (Exception e) {

        }

        try {
            if (hour.length() >= 2) {
                prizeMainLayoutBinding.txtHour1.setText(hour.substring(0, 1));
                prizeMainLayoutBinding.txtHour2.setText(hour.substring(1, 2));
            } else {
                prizeMainLayoutBinding.txtHour1.setText("0");
                prizeMainLayoutBinding.txtHour2.setText(hour);
            }
        } catch (Exception e) {

        }

        try {
            if (min.length() >= 2) {
                prizeMainLayoutBinding.txtMinute1.setText(min.substring(0, 1));
                prizeMainLayoutBinding.txtMinute2.setText(min.substring(1, 2));
            } else {
                prizeMainLayoutBinding.txtMinute1.setText("0");
                prizeMainLayoutBinding.txtMinute2.setText(min);
            }
        } catch (Exception e) {

        }

        try {
            if (sec.length() >= 2) {
                prizeMainLayoutBinding.txtSecond1.setText(sec.substring(0, 1));
                prizeMainLayoutBinding.txtSecond2.setText(sec.substring(1, 2));
            } else {
                prizeMainLayoutBinding.txtSecond1.setText("0");
                prizeMainLayoutBinding.txtSecond2.setText(sec);
            }
        } catch (Exception e) {

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
    private class HttpAsyncTaskprizeHistory extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PrizeActivity.this);
            dialog.setMessage("Fetching event...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            /*try {
                jsonObject.accumulate("userid",   UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return Utility.GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("response_prize_All", "" + result);
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONObject response = new JSONObject(result);
                prizeList.clear();
                JSONArray jsonArray = response.getJSONArray("prize_details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject iCon = jsonArray.getJSONObject(i);
                    Prize prizeModel = new Prize();

                    //prizeHistoryModel.setUserId(iCon.getString("userid"));
                    prizeModel.setPrize_ID(iCon.getString("Prize_ID"));
                    prizeModel.setPrize_Name(iCon.getString("Prize_Name"));
                    prizeModel.setPrize_Image(iCon.getString("Prize_Image"));
                    prizeModel.setCard_Name_1(iCon.getString("Card_Name_1"));
                    prizeModel.setCard_Name_2(iCon.getString("Card_Name_2"));
                    prizeList.add(prizeModel);

                }
                prizeAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
