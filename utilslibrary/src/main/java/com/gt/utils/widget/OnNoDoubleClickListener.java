package com.gt.utils.widget;

import android.view.View;

import java.util.Calendar;

/**
 * 防止点击事件重复调用
 * Created by Administrator on 2017/7/7.
 */

public abstract class OnNoDoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
