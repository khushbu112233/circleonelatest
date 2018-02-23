package com.circle8.circleOne.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.circle8.circleOne.Adapter.PrizeHistoryAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.databinding.PrizeHistoryLayoutBinding;

public class PrizeHistoryActivity extends AppCompatActivity {
    PrizeHistoryLayoutBinding prizeHistoryLayoutBinding;
    PrizeHistoryAdapter prizeHistoryAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prizeHistoryLayoutBinding = DataBindingUtil.setContentView(this, R.layout.prize_history_layout);
        if(Pref.getValue(PrizeHistoryActivity.this,"History","").equalsIgnoreCase("2"))
        {
            prizeHistoryAdapter = new PrizeHistoryAdapter(PrizeHistoryActivity.this,LuckyDrawActivity.prizeHistorys);
            prizeHistoryLayoutBinding.lstPrizeHistory.setAdapter(prizeHistoryAdapter);
            prizeHistoryAdapter.notifyDataSetChanged();
        }else if(Pref.getValue(PrizeHistoryActivity.this,"History","").equalsIgnoreCase("1"))
        {
            prizeHistoryAdapter = new PrizeHistoryAdapter(PrizeHistoryActivity.this,LuckyDrawActivity.prizeHistorysAll);
            prizeHistoryLayoutBinding.lstPrizeHistory.setAdapter(prizeHistoryAdapter);
            prizeHistoryAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
