package com.gt.utils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.gt.utils.R;

public class BgCLayout extends ConstraintLayout {

    public ViewBgControl viewBgControl;
    private OnClickListener onClickListener;

    public BgCLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgCLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewBgControl = new ViewBgControl(this);
        viewBgControl.initBgStyle(context.obtainStyledAttributes(attrs, R.styleable.BgCLayout));
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
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
        if (onClickListener != null) {
            if (!hasOnClickListeners()) {
                return !super.dispatchTouchEvent(ev) ? true : super.dispatchTouchEvent(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
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

    //**********color_def************
    public void setSolid_start_color(@ColorInt int resId) {
        viewBgControl.defStyle.solid_start_color = resId;
        invalidateDef();
    }

    public void setSolid_end_color(@ColorInt int resId) {
        viewBgControl.defStyle.solid_end_color = resId;
        invalidateDef();
    }

    public void setSolid_color(@ColorInt int resId) {
        viewBgControl.defStyle.solid_start_color = resId;
        viewBgControl.defStyle.solid_end_color = resId;
        invalidateDef();
    }

    public void setStroke_start_color(@ColorInt int resId) {
        viewBgControl.defStyle.stroke_start_color = resId;
        invalidateDef();
    }

    public void setStroke_end_color(@ColorInt int resId) {
        viewBgControl.defStyle.stroke_end_color = resId;
        invalidateDef();
    }

    public void setStroke_color(@ColorInt int resId) {
        viewBgControl.defStyle.stroke_start_color = resId;
        viewBgControl.defStyle.stroke_end_color = resId;
        invalidateDef();
    }

    public void invalidateDef(){
        viewBgControl.setCurrentStyle(viewBgControl.defStyle);
        invalidate();
    }
    //**********color_def************

    //**********color_pressed************
    public void setSolid_start_color_pressed(@ColorInt int resId) {
        viewBgControl.pressedStyle.solid_start_color = resId;
    }

    public void setSolid_end_color_pressed(@ColorInt int resId) {
        viewBgControl.pressedStyle.solid_end_color = resId;
    }

    public void setSolid_color_pressed(@ColorInt int resId) {
        viewBgControl.pressedStyle.solid_start_color = resId;
        viewBgControl.pressedStyle.solid_end_color = resId;
    }

    public void setStroke_start_color_pressed(@ColorInt int resId) {
        viewBgControl.pressedStyle.stroke_start_color = resId;
    }

    public void setStroke_end_color_pressed(@ColorInt int resId) {
        viewBgControl.pressedStyle.stroke_end_color = resId;
    }

    public void setStroke_color_pressed(@ColorInt int resId) {
        viewBgControl.pressedStyle.stroke_start_color = resId;
        viewBgControl.pressedStyle.stroke_end_color = resId;
    }
    //**********color_pressed************

    //**********color_focused************
    public void setSolid_start_color_focused(@ColorInt int resId) {
        viewBgControl.focusedStyle.solid_start_color = resId;
    }

    public void setSolid_end_color_focused(@ColorInt int resId) {
        viewBgControl.focusedStyle.solid_end_color = resId;
    }

    public void setSolid_color_focused(@ColorInt int resId) {
        viewBgControl.focusedStyle.solid_start_color = resId;
        viewBgControl.focusedStyle.solid_end_color = resId;
    }

    public void setStroke_start_color_focused(@ColorInt int resId) {
        viewBgControl.focusedStyle.stroke_start_color = resId;
    }

    public void setStroke_end_color_focused(@ColorInt int resId) {
        viewBgControl.focusedStyle.stroke_end_color = resId;
    }

    public void setStroke_color_focused(@ColorInt int resId) {
        viewBgControl.focusedStyle.stroke_start_color = resId;
        viewBgControl.focusedStyle.stroke_end_color = resId;
    }
    //**********color_focused************

    //**********color_checked************
    public void setSolid_start_color_checked(@ColorInt int resId) {
        viewBgControl.checkedStyle.solid_start_color = resId;
    }

    public void setSolid_end_color_checked(@ColorInt int resId) {
        viewBgControl.checkedStyle.solid_end_color = resId;
    }

    public void setSolid_color_checked(@ColorInt int resId) {
        viewBgControl.checkedStyle.solid_start_color = resId;
        viewBgControl.checkedStyle.solid_end_color = resId;
    }

    public void setStroke_start_color_checked(@ColorInt int resId) {
        viewBgControl.checkedStyle.stroke_start_color = resId;
    }

    public void setStroke_end_color_checked(@ColorInt int resId) {
        viewBgControl.checkedStyle.stroke_end_color = resId;
    }

    public void setStroke_color_checked(@ColorInt int resId) {
        viewBgControl.checkedStyle.stroke_start_color = resId;
        viewBgControl.checkedStyle.stroke_end_color = resId;
    }
    //**********color_checked************

    //**********color_no_enabled************
    public void setSolid_start_color_no_enabled(@ColorInt int resId) {
        viewBgControl.noEnabledStyle.solid_start_color = resId;
    }

    public void setSolid_end_color_no_enabled(@ColorInt int resId) {
        viewBgControl.noEnabledStyle.solid_end_color = resId;
    }

    public void setSolid_color_no_enabled(@ColorInt int resId) {
        viewBgControl.noEnabledStyle.solid_start_color = resId;
        viewBgControl.noEnabledStyle.solid_end_color = resId;
    }

    public void setStroke_start_color_no_enabled(@ColorInt int resId) {
        viewBgControl.noEnabledStyle.stroke_start_color = resId;
    }

    public void setStroke_end_color_no_enabled(@ColorInt int resId) {
        viewBgControl.noEnabledStyle.stroke_end_color = resId;
    }

    public void setStroke_color_no_enabled(@ColorInt int resId) {
        viewBgControl.noEnabledStyle.stroke_start_color = resId;
        viewBgControl.noEnabledStyle.stroke_end_color = resId;
    }
    //**********color_no_enabled************
}