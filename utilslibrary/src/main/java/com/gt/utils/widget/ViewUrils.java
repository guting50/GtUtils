package com.gt.utils.widget;

import android.view.View;

public class ViewUrils {

    /*
     * 判断点击位置是否位于该View内
     * @param x x坐标
     * @param y y坐标
     * @return
     */
    public static boolean findTopChildUnder(View view, float x, float y) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        int left = position[0];
        int top = position[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (x >= left && x <= right && y >= top && y <= bottom) {
            return true;
        }
        return false;
    }
}
