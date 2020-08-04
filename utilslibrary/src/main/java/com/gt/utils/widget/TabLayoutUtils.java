package com.gt.utils.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TabLayoutUtils {

    public static void reflex(final TabLayout tabLayout, final int mW, final int mH, @ColorInt final int color) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                //拿到tabLayout的mTabStrip属性
                tabLayout.setSelectedTabIndicatorHeight(0);
                final LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                mTabStrip.setBackgroundDrawable(new Drawable() {
                    Paint paint;
                    float width;
                    float height;

                    {
                        paint = new Paint();
                        paint.setColor(color);
                        float density = mTabStrip.getResources().getDisplayMetrics().density;
                        //这两个留白可以根据需求更改
                        width = mW * density;
                        height = mH * density;
                        mTabStrip.setPadding(0, 0, 0, (int) height);
                    }

                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        //这里通过反射获取SlidingTabStrip的两个变量，源代码画的是下划线，我们现在画的是带圆角的矩形
                        int mIndicatorLeft = getIntValue("indicatorLeft");
                        int mIndicatorRight = getIntValue("indicatorRight");
                        int h = mTabStrip.getHeight();
                        if (mIndicatorLeft >= 0 && mIndicatorRight > mIndicatorLeft) {
                            int centerPoint = mIndicatorLeft + (mIndicatorRight - mIndicatorLeft) / 2;
                            canvas.drawRoundRect(new RectF(centerPoint - width / 2, h - (int) height,
                                    centerPoint + width / 2, h), h, h, paint);

                        }
                    }

                    int getIntValue(String name) {
                        try {
                            Field f = mTabStrip.getClass().getDeclaredField(name);
                            f.setAccessible(true);
                            Object obj = f.get(mTabStrip);
                            return (Integer) obj;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return 0;
                    }

                    @Override
                    public void setAlpha(int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.UNKNOWN;
                    }
                });
            }
        });
    }
}
