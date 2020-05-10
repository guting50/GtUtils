package com.gt.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.gt.utils.R;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class BgTextView extends TextView {
    private class Style {
        private int textColor;
        private int textSize;
        private String text;
    }

    public Style currentStyle, defStyle = new Style(), noEnabledStyle = new Style(), checkedStyle = new Style();

    public BgTextView(Context context) {
        this(context, null);
    }

    public BgTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrVal = attrs.getAttributeValue(i);
            Log.e("BgTextView", "attrName = " + attrName + " , attrVal = " + attrVal);
            if (TextUtils.equals("textColor", attrName)) {
                defStyle.textColor = Color.parseColor(attrVal);
                defStyle.textColor = attrs.getAttributeIntValue(i, 0);
            }
            if (TextUtils.equals("textSize", attrName)) {
                float a = attrs.getAttributeFloatValue(i, 0);
//                defStyle.textSize = attrs.getAttributeIntValue(i, 0);
                defStyle.textSize = attrs.getAttributeIntValue(i, 0);
            }
            if (TextUtils.equals("text", attrName)) {
                defStyle.text = attrVal;
            }
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BgTextView);
        noEnabledStyle.textColor = typedArray.getColor(R.styleable.BgTextView_textColor_no_enabled, Color.TRANSPARENT);
        noEnabledStyle.textSize = typedArray.getDimensionPixelSize(R.styleable.BgTextView_textSize_no_enabled, defStyle.textSize);
        noEnabledStyle.text = typedArray.getString(R.styleable.BgTextView_text_no_enabled);

        typedArray.recycle();//释放资源
    }
}
