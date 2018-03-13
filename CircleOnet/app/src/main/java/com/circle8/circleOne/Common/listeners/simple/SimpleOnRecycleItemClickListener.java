package com.circle8.circleOne.Common.listeners.simple;

import android.view.View;

import com.circle8.circleOne.Common.listeners.OnRecycleItemClickListener;

public class SimpleOnRecycleItemClickListener<T> implements OnRecycleItemClickListener<T> {

    @Override
    public void onItemClicked(View view, T entity, int position) {
    }

    @Override
    public void onItemLongClicked(View view, T entity, int position) {
    }
}