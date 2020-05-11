package com.gt.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.gt.utils.GsonUtils;
import com.gt.utils.R;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class BgTextView extends TextView {
    private class Style {
        private int textColor;
        private int textSize;
        private String text;
    }

    public Style currentStyle, defStyle = new Style(), noEnabledStyle = new Style(), checkedStyle = new Style(), focusedStyle = new Style(), pressedStyle = new Style();
    private boolean checked, focused;
    private OnClickListener onClickListener;

    public BgTextView(Context context) {
        this(context, null);
    }

    public BgTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        defStyle.textColor = getTextColors().getDefaultColor();
        defStyle.textSize = (int) getTextSize();
        defStyle.text = getText().toString();
        currentStyle = GsonUtils.getGson().fromJson(GsonUtils.getGson().toJson(defStyle), Style.class);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BgTextView);
        noEnabledStyle.textColor = typedArray.getColor(R.styleable.BgTextView_textColor_no_enabled, -1);
        noEnabledStyle.textSize = typedArray.getDimensionPixelSize(R.styleable.BgTextView_textSize_no_enabled, -1);
        noEnabledStyle.text = typedArray.getString(R.styleable.BgTextView_text_no_enabled);

        checkedStyle.textColor = typedArray.getColor(R.styleable.BgTextView_textColor_checked, -1);
        checkedStyle.textSize = typedArray.getDimensionPixelSize(R.styleable.BgTextView_textSize_checked, -1);
        checkedStyle.text = typedArray.getString(R.styleable.BgTextView_text_checked);

        pressedStyle.textColor = typedArray.getColor(R.styleable.BgTextView_textColor_pressed, -1);
        pressedStyle.textSize = typedArray.getDimensionPixelSize(R.styleable.BgTextView_textSize_pressed, -1);
        pressedStyle.text = typedArray.getString(R.styleable.BgTextView_text_pressed);

        focusedStyle.textColor = typedArray.getColor(R.styleable.BgTextView_textColor_focused, -1);
        focusedStyle.textSize = typedArray.getDimensionPixelSize(R.styleable.BgTextView_textSize_focused, -1);
        focusedStyle.text = typedArray.getString(R.styleable.BgTextView_text_focused);

        typedArray.recycle();//释放资源
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setCurrentStyle(defStyle);
        } else {
            setCurrentStyle(noEnabledStyle);
        }
    }

    public void setChecked(boolean checked) {
        if (isEnabled()) {
            this.checked = checked;
            if (this.focused) {
                //  当该View是获取到焦点状态时，设置成focusedStyle
                //当没有设置focusedStyle样式时，显示checkedStyle样式
                setCurrentStyle(focusedStyle, checkedStyle);
            } else {
                if (this.checked) {
                    setCurrentStyle(checkedStyle);
                } else {
                    setCurrentStyle(defStyle);
                }
            }
        }
    }

    public void SetPressed(boolean pressed) {
        if (isEnabled()) {
            if (pressed) {
                //当没有设置pressedStyle样式时，样式不变
                setCurrentStyle(pressedStyle, currentStyle);
            } else {
                if (this.focused) {//  当该View是获取到焦点状态时，抬起后设置成focusedStyle
                    setCurrentStyle(focusedStyle);
                } else if (this.checked) { //  当该View是选中状态时，抬起后设置成checkedStyle
                    setCurrentStyle(checkedStyle);
                } else
                    setCurrentStyle(defStyle);
            }
        }
    }

    public void setFocused(boolean focused) {
        if (isEnabled()) {
            if (this.focused != focused) {
                this.focused = focused;
                if (this.focused) {
                    //当没有设置focusedStyle样式时，样式不变
                    setCurrentStyle(focusedStyle, currentStyle);
                    // 当设置允许获取焦点后，要点击两下才能执行onClick，所以当selected为true时，执行onClick
                    onBgClick(this);
                } else {
                    if (this.checked) { //  当该View是选中状态时，失去焦点后设置成checkedStyle
                        setCurrentStyle(checkedStyle);
                    } else
                        setCurrentStyle(defStyle);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                SetPressed(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                SetPressed(false);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!ViewUrils.findTopChildUnder(this, ev.getRawX(), ev.getRawY())) {
                    SetPressed(false);
                }
                break;
        }
        if (!hasOnClickListeners()) {
            return !super.dispatchTouchEvent(ev) ? true : super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        setFocused(gainFocus);
    }

    private void onBgClick(View view) {
        setChecked(!checked);
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public void setCurrentStyle(Style style) {
        setCurrentStyle(style, defStyle);
    }

    public void setCurrentStyle(Style style, Style defStyle) {
        setCurrentStyle(style, defStyle, this.defStyle);
    }

    public void setCurrentStyle(Style style, Style assistStyle, Style defStyle) {
        if (style.textColor != -1)
            currentStyle.textColor = style.textColor;
        else if (assistStyle.textColor != -1)
            currentStyle.textColor = assistStyle.textColor;
        else
            currentStyle.textColor = defStyle.textColor;

        if (style.textSize != -1)
            currentStyle.textSize = style.textSize;
        else if (assistStyle.textSize != -1)
            currentStyle.textSize = assistStyle.textSize;
        else
            currentStyle.textSize = defStyle.textSize;

        if (!TextUtils.isEmpty(style.text))
            currentStyle.text = style.text;
        else if (!TextUtils.isEmpty(assistStyle.text))
            currentStyle.text = assistStyle.text;
        else
            currentStyle.text = defStyle.text;

        setTextColor(currentStyle.textColor);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, currentStyle.textSize);
        setText(currentStyle.text);
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
    }
}
