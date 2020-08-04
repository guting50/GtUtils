package com.gt.utils.widget.multigridview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.gt.utils.R;


/**
 * 带进度条显示的图片
 * Created by Administrator on 2016/11/28.
 */
@SuppressLint("AppCompatCustomView")
public class ProcessImageView extends ImageView {

    private Paint mPaint;// 画笔
    int width = 0;
    int height = 0;
    Context context = null;
    int progress = 0;
    boolean showProgress;

    public ProcessImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ProcessImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);

        Rect rect = new Rect();
        mPaint.getTextBounds("100%", 0, "100%".length(), rect);// 确定文字的宽度

        if (!showProgress || progress >= 100) {
            mPaint.setColor(Color.parseColor("#00000000"));// 全透明
            canvas.drawRect(0, getHeight() - getHeight() * progress / 100, getWidth(), getHeight(), mPaint);
        } else {
            canvas.drawText(progress + "%", getWidth() / 2, getHeight() / 2 + rect.height() / 2, mPaint);
            mPaint.setColor(Color.parseColor("#10000000"));// 半透明
            canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * progress / 100, mPaint);
        }

        mPaint.setTextSize(60);
        mPaint.setColor(getResources().getColor(R.color.text_process_image_view));
        mPaint.setStrokeWidth(8);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setProgress(int progress) {
        this.progress = progress >= 95 ? 100 : progress;
        postInvalidate();
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }
}
