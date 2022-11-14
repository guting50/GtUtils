package com.gt.utils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.gt.utils.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class BgLLayout extends LinearLayout {

    public ViewBgControl viewBgControl;

    public BgLLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgLLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewBgControl = new ViewBgControl(this);
        viewBgControl.initBgStyle(context.obtainStyledAttributes(attrs, R.styleable.BgLLayout));
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(v -> {
            viewBgControl.setChecked(!viewBgControl.checked);
            if (l != null) {
                l.onClick(v);
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

    public void setBackgroundColor(@ColorInt int resId) {
        setBackgroundColors(resId);
    }

    public void setBackgroundColors(@ColorInt int... resId) {
        if (resId != null) {
            if (resId.length == 1) {
                viewBgControl.defStyle.solid_start_color = resId[0];
                viewBgControl.defStyle.solid_end_color = resId[0];
            } else if (resId.length == 2) {
                viewBgControl.defStyle.solid_start_color = resId[0];
                viewBgControl.defStyle.solid_end_color = resId[1];
            }
            viewBgControl.setCurrentStyle(viewBgControl.defStyle);
            invalidate();
        }
    }
}