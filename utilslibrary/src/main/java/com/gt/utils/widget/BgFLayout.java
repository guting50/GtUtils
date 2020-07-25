package com.gt.utils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.gt.utils.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class BgFLayout extends FrameLayout {

    public ViewBgControl viewBgControl;
    private OnClickListener onClickListener;

    public BgFLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgFLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewBgControl = new ViewBgControl(this);
        viewBgControl.initBgStyle(context.obtainStyledAttributes(attrs, R.styleable.BgFLayout));

        //当设置在触摸模式下可以获取焦点时，如果不设置点击事件，onFocusChanged不回调
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBgControl.setChecked(!viewBgControl.checked);
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void dispatchDraw(Canvas canvas) {
        viewBgControl.dispatchDraw(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        viewBgControl.dispatchTouchEvent(ev);
        if (!hasOnClickListeners()) {
            return !super.dispatchTouchEvent(ev) ? true : super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        viewBgControl.setFocused(gainFocus);
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        onClickListener = l;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        viewBgControl.setEnabled(enabled);
    }

    public void setChecked(boolean checked) {
        viewBgControl.setChecked(checked);
    }

    public void SetPressed(boolean pressed) {
        viewBgControl.SetPressed(pressed);
    }

    public void setFocused(boolean focused) {
        viewBgControl.setFocused(focused);
    }

    public void setCurrentStyle(ViewBgControl.Style style) {
        viewBgControl.setCurrentStyle(style);
    }

    public void setBackgroundColor(@ColorInt int... resId) {
        if (resId != null) {
            if (resId.length == 1) {
                viewBgControl.currentStyle.solid_start_color = resId[0];
                viewBgControl.currentStyle.solid_end_color = resId[0];
            } else if (resId.length == 2) {
                viewBgControl.currentStyle.solid_start_color = resId[0];
                viewBgControl.currentStyle.solid_end_color = resId[1];
            }
            invalidate();
        }
    }
}