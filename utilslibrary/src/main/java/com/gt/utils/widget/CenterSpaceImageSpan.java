package com.gt.utils.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

public class CenterSpaceImageSpan extends ImageSpan {

    public CenterSpaceImageSpan(Drawable drawable) {
        super(drawable);

    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {
        Drawable b = getDrawable();
        int transY = (top + bottom) / 2 - (b.getBounds().top + b.getBounds().bottom) / 2;
        canvas.save();
        canvas.translate(x, transY);//绘制图片位移一段距离
        b.draw(canvas);
        canvas.restore();
    }
}