package com.gt.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.gt.utils.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


/*
 * 自定义可设置背景的帧布局
 * 自定义属性
    <declare-styleable name="BgFrameLayout">
        <!--default-->
        <attr name="solid_color" format="color" /><!--填充色-->
        <attr name="solid_start_color" format="color" /><!--填充渐变开始颜色-->
        <attr name="solid_end_color" format="color" /><!--填充渐变结束颜色-->
        <attr name="solid_gradual_orientation" format="enum"><!--填充渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_color" format="color" /><!--边框颜色-->
        <attr name="stroke_start_color" format="color" /><!--边框渐变开始颜色-->
        <attr name="stroke_end_color" format="color" /><!--边框渐变结束颜色-->
        <attr name="stroke_gradual_orientation" format="enum"><!--边框渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_width" format="dimension" /><!--边框宽度-->
        <attr name="stroke_dash_gap" format="dimension" /><!--间隔-->
        <attr name="stroke_dash_width" format="dimension" /><!--点的大小-->
        <attr name="corners_radius" format="dimension" /><!--圆角弧度-->
        <attr name="corners_radius_left_top" format="dimension" /><!--圆角弧度 左上角-->
        <attr name="corners_radius_right_top" format="dimension" /><!--圆角弧度 右上角-->
        <attr name="corners_radius_left_bottom" format="dimension" /><!--圆角弧度 左下角-->
        <attr name="corners_radius_right_bottom" format="dimension" /><!--圆角弧度 右下角-->

        <!--noEnabled-->
        <attr name="solid_color_no_enabled" format="color" /><!--填充色-->
        <attr name="solid_start_color_no_enabled" format="color" /><!--填充渐变开始颜色-->
        <attr name="solid_end_color_no_enabled" format="color" /><!--填充渐变结束颜色-->
        <attr name="solid_gradual_orientation_no_enabled" format="enum"><!--填充渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_color_no_enabled" format="color" /><!--边框颜色-->
        <attr name="stroke_start_color_no_enabled" format="color" /><!--边框渐变开始颜色-->
        <attr name="stroke_end_color_no_enabled" format="color" /><!--边框渐变结束颜色-->
        <attr name="stroke_gradual_orientation_no_enabled" format="enum"><!--边框渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_width_no_enabled" format="dimension" /><!--边框宽度-->
        <attr name="stroke_dash_gap_no_enabled" format="dimension" /><!--间隔-->
        <attr name="stroke_dash_width_no_enabled" format="dimension" /><!--点的大小-->
        <attr name="corners_radius_no_enabled" format="dimension" /><!--圆角弧度-->
        <attr name="corners_radius_left_top_no_enabled" format="dimension" /><!--圆角弧度 左上角-->
        <attr name="corners_radius_right_top_no_enabled" format="dimension" /><!--圆角弧度 右上角-->
        <attr name="corners_radius_left_bottom_no_enabled" format="dimension" /><!--圆角弧度 左下角-->
        <attr name="corners_radius_right_bottom_no_enabled" format="dimension" /><!--圆角弧度 右下角-->

        <!--checked-->
        <attr name="solid_color_checked" format="color" /><!--填充色-->
        <attr name="solid_start_color_checked" format="color" /><!--填充渐变开始颜色-->
        <attr name="solid_end_color_checked" format="color" /><!--填充渐变结束颜色-->
        <attr name="solid_gradual_orientation_checked" format="enum"><!--填充渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_color_checked" format="color" /><!--边框颜色-->
        <attr name="stroke_start_color_checked" format="color" /><!--边框渐变开始颜色-->
        <attr name="stroke_end_color_checked" format="color" /><!--边框渐变结束颜色-->
        <attr name="stroke_gradual_orientation_checked" format="enum"><!--边框渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_width_checked" format="dimension" /><!--边框宽度-->
        <attr name="stroke_dash_gap_checked" format="dimension" /><!--间隔-->
        <attr name="stroke_dash_width_checked" format="dimension" /><!--点的大小-->
        <attr name="corners_radius_checked" format="dimension" /><!--圆角弧度-->
        <attr name="corners_radius_left_top_checked" format="dimension" /><!--圆角弧度 左上角-->
        <attr name="corners_radius_right_top_checked" format="dimension" /><!--圆角弧度 右上角-->
        <attr name="corners_radius_left_bottom_checked" format="dimension" /><!--圆角弧度 左下角-->
        <attr name="corners_radius_right_bottom_checked" format="dimension" /><!--圆角弧度 右下角-->

        <!--pressed-->
        <attr name="solid_color_pressed" format="color" /><!--填充色-->
        <attr name="solid_start_color_pressed" format="color" /><!--填充渐变开始颜色-->
        <attr name="solid_end_color_pressed" format="color" /><!--填充渐变结束颜色-->
        <attr name="solid_gradual_orientation_pressed" format="enum"><!--填充渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_color_pressed" format="color" /><!--边框颜色-->
        <attr name="stroke_start_color_pressed" format="color" /><!--边框渐变开始颜色-->
        <attr name="stroke_end_color_pressed" format="color" /><!--边框渐变结束颜色-->
        <attr name="stroke_gradual_orientation_pressed" format="enum"><!--边框渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_width_pressed" format="dimension" /><!--边框宽度-->
        <attr name="stroke_dash_gap_pressed" format="dimension" /><!--间隔-->
        <attr name="stroke_dash_width_pressed" format="dimension" /><!--点的大小-->
        <attr name="corners_radius_pressed" format="dimension" /><!--圆角弧度-->
        <attr name="corners_radius_left_top_pressed" format="dimension" /><!--圆角弧度 左上角-->
        <attr name="corners_radius_right_top_pressed" format="dimension" /><!--圆角弧度 右上角-->
        <attr name="corners_radius_left_bottom_pressed" format="dimension" /><!--圆角弧度 左下角-->
        <attr name="corners_radius_right_bottom_pressed" format="dimension" /><!--圆角弧度 右下角-->
    </declare-styleable>
 */
public class BgFrameLayout extends FrameLayout {

    private class Style {
        private int solid_color;
        private int solid_start_color;
        private int solid_end_color;
        private int solid_gradual_orientation;
        private int stroke_color;
        private int stroke_start_color;
        private int stroke_end_color;
        private int stroke_gradual_orientation;
        private float stroke_width;
        private float stroke_dash_gap;
        private float stroke_dash_width;
        private float corners_radius;
        private float corners_radius_left_top;
        private float corners_radius_right_top;
        private float corners_radius_left_bottom;
        private float corners_radius_right_bottom;
    }

    private Style currentStyle, defStyle = new Style(), noEnabledStyle = new Style(), checkedStyle = new Style(), selectedStyle = new Style(), pressedStyle = new Style();
    private OnClickListener onClickListener;

    public BgFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BgFrameLayout);
        defStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color, Color.TRANSPARENT);
        defStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color, defStyle.solid_color);
        defStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color, defStyle.solid_color);
        defStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation, 0);
        defStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color, Color.TRANSPARENT);
        defStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color, defStyle.stroke_color);
        defStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color, defStyle.stroke_color);
        defStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation, 0);
        defStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width, 0.0f);
        defStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap, 0);
        defStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width, 0);
        defStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius, 0);
        defStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top, defStyle.corners_radius);
        defStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top, defStyle.corners_radius);
        defStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom, defStyle.corners_radius);
        defStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom, defStyle.corners_radius);
        currentStyle = defStyle;

        noEnabledStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color_no_enabled, Color.TRANSPARENT);
        noEnabledStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color_no_enabled, noEnabledStyle.solid_color == Color.TRANSPARENT ? defStyle.solid_start_color : noEnabledStyle.solid_color);
        noEnabledStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color_no_enabled, noEnabledStyle.solid_color == Color.TRANSPARENT ? defStyle.solid_end_color : noEnabledStyle.solid_color);
        noEnabledStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation_no_enabled, defStyle.solid_gradual_orientation);
        noEnabledStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color_no_enabled, Color.TRANSPARENT);
        noEnabledStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color_no_enabled, noEnabledStyle.stroke_color == Color.TRANSPARENT ? defStyle.stroke_start_color : noEnabledStyle.stroke_color);
        noEnabledStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color_no_enabled, noEnabledStyle.stroke_color == Color.TRANSPARENT ? defStyle.stroke_end_color : noEnabledStyle.stroke_color);
        noEnabledStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation_no_enabled, defStyle.stroke_gradual_orientation);
        noEnabledStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width_no_enabled, defStyle.stroke_width);
        noEnabledStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap_no_enabled, defStyle.stroke_dash_gap);
        noEnabledStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width_no_enabled, defStyle.stroke_dash_width);
        noEnabledStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_no_enabled, 0);
        noEnabledStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top_no_enabled, noEnabledStyle.corners_radius == 0 ? defStyle.corners_radius_left_top : noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top_no_enabled, noEnabledStyle.corners_radius == 0 ? defStyle.corners_radius_right_top : noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom_no_enabled, noEnabledStyle.corners_radius == 0 ? defStyle.corners_radius_left_bottom : noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom_no_enabled, noEnabledStyle.corners_radius == 0 ? defStyle.corners_radius_right_bottom : noEnabledStyle.corners_radius);

        checkedStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color_checked, Color.TRANSPARENT);
        checkedStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color_checked, checkedStyle.solid_color == Color.TRANSPARENT ? defStyle.solid_start_color : checkedStyle.solid_color);
        checkedStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color_checked, checkedStyle.solid_color == Color.TRANSPARENT ? defStyle.solid_end_color : checkedStyle.solid_color);
        checkedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation_checked, defStyle.solid_gradual_orientation);
        checkedStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color_checked, Color.TRANSPARENT);
        checkedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color_checked, checkedStyle.stroke_color == Color.TRANSPARENT ? defStyle.stroke_start_color : checkedStyle.stroke_color);
        checkedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color_checked, checkedStyle.stroke_color == Color.TRANSPARENT ? defStyle.stroke_end_color : checkedStyle.stroke_color);
        checkedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation_checked, defStyle.stroke_gradual_orientation);
        checkedStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width_checked, defStyle.stroke_width);
        checkedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap_checked, defStyle.stroke_dash_gap);
        checkedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width_checked, defStyle.stroke_dash_width);
        checkedStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_checked, 0);
        checkedStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top_checked, checkedStyle.corners_radius == 0 ? defStyle.corners_radius_left_top : checkedStyle.corners_radius);
        checkedStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top_checked, checkedStyle.corners_radius == 0 ? defStyle.corners_radius_right_top : checkedStyle.corners_radius);
        checkedStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom_checked, checkedStyle.corners_radius == 0 ? defStyle.corners_radius_left_bottom : checkedStyle.corners_radius);
        checkedStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom_checked, checkedStyle.corners_radius == 0 ? defStyle.corners_radius_right_bottom : checkedStyle.corners_radius);

        pressedStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color_pressed, Color.TRANSPARENT);
        pressedStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color_pressed, pressedStyle.solid_color == Color.TRANSPARENT ? checkedStyle.solid_start_color : pressedStyle.solid_color);
        pressedStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color_pressed, pressedStyle.solid_color == Color.TRANSPARENT ? checkedStyle.solid_end_color : pressedStyle.solid_color);
        pressedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation_pressed, checkedStyle.solid_gradual_orientation);
        pressedStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color_pressed, Color.TRANSPARENT);
        pressedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color_pressed, pressedStyle.stroke_color == Color.TRANSPARENT ? checkedStyle.stroke_start_color : pressedStyle.stroke_color);
        pressedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color_pressed, pressedStyle.stroke_color == Color.TRANSPARENT ? checkedStyle.stroke_end_color : pressedStyle.stroke_color);
        pressedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation_pressed, checkedStyle.stroke_gradual_orientation);
        pressedStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width_pressed, checkedStyle.stroke_width);
        pressedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap_pressed, checkedStyle.stroke_dash_gap);
        pressedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width_pressed, checkedStyle.stroke_dash_width);
        pressedStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_pressed, 0);
        pressedStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top_pressed, pressedStyle.corners_radius == 0 ? checkedStyle.corners_radius_left_top : pressedStyle.corners_radius);
        pressedStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top_pressed, pressedStyle.corners_radius == 0 ? checkedStyle.corners_radius_right_top : pressedStyle.corners_radius);
        pressedStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom_pressed, pressedStyle.corners_radius == 0 ? checkedStyle.corners_radius_left_bottom : pressedStyle.corners_radius);
        pressedStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom_pressed, pressedStyle.corners_radius == 0 ? checkedStyle.corners_radius_right_bottom : pressedStyle.corners_radius);

        typedArray.recycle();//释放资源

        selectedStyle = checkedStyle;
//        setFocusableInTouchMode(true);

        //当设置在触摸模式下可以获取焦点时，如果不设置点击事件，onFocusChanged不回调
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    private boolean checked;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Path path = new Path();

        //画边框
        {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(currentStyle.stroke_width);
            int startX = 0, startY = 0, endX = this.getWidth(), endY = this.getHeight();
            if (currentStyle.stroke_gradual_orientation == 0) {
                startY = endY = this.getHeight() / 2;
            } else if (currentStyle.stroke_gradual_orientation == 1) {
                startX = endX = this.getWidth() / 2;
            }
            Shader mShader = new LinearGradient(startX, startY, endX, endY, new int[]{currentStyle.stroke_start_color, currentStyle.stroke_end_color}, null, Shader.TileMode.CLAMP);
            paint.setShader(mShader);

            if (currentStyle.stroke_dash_width > 0)
                paint.setPathEffect(new DashPathEffect(new float[]{currentStyle.stroke_dash_width, currentStyle.stroke_dash_gap}, 0));
            path.addRoundRect(new RectF(currentStyle.stroke_width / 2, currentStyle.stroke_width / 2,
                            this.getWidth() - currentStyle.stroke_width / 2, this.getHeight() - currentStyle.stroke_width / 2),
                    new float[]{currentStyle.corners_radius_left_top, currentStyle.corners_radius_left_top, currentStyle.corners_radius_right_top, currentStyle.corners_radius_right_top,
                            currentStyle.corners_radius_right_bottom, currentStyle.corners_radius_right_bottom, currentStyle.corners_radius_left_bottom, currentStyle.corners_radius_left_bottom},
                    Path.Direction.CW);
            canvas.drawPath(path, paint);
        }

        //画背景
        {
            paint.setStyle(Paint.Style.FILL);
            //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。
            // 然后那个数组是渐变的颜色。
            // 下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
            int startX = 0, startY = 0, endX = this.getWidth(), endY = this.getHeight();
            if (currentStyle.solid_gradual_orientation == 0) {
                startY = endY = this.getHeight() / 2;
            } else if (currentStyle.solid_gradual_orientation == 1) {
                startX = endX = this.getWidth() / 2;
            }
            Shader mShader = new LinearGradient(startX, startY, endX, endY, new int[]{currentStyle.solid_start_color, currentStyle.solid_end_color}, null, Shader.TileMode.CLAMP);
            paint.setShader(mShader);

            path.addRoundRect(new RectF(currentStyle.stroke_width, currentStyle.stroke_width,
                            this.getWidth() - currentStyle.stroke_width, this.getHeight() - currentStyle.stroke_width),
                    new float[]{currentStyle.corners_radius_left_top, currentStyle.corners_radius_left_top, currentStyle.corners_radius_right_top, currentStyle.corners_radius_right_top,
                            currentStyle.corners_radius_right_bottom, currentStyle.corners_radius_right_bottom, currentStyle.corners_radius_left_bottom, currentStyle.corners_radius_left_bottom},
                    Path.Direction.CW);
            canvas.drawPath(path, paint);
        }
        super.dispatchDraw(canvas);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            currentStyle = defStyle;
        } else {
            currentStyle = noEnabledStyle;
        }
        invalidate();
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked) {
            currentStyle = checkedStyle;
        } else {
            currentStyle = defStyle;
        }
        invalidate();
    }

    public void SetPressed(boolean pressed) {
//        super.setPressed(pressed);
        if (pressed) {
            currentStyle = pressedStyle;
        } else {
            currentStyle = defStyle;
        }
        invalidate();
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            currentStyle = selectedStyle;
            // 当设置允许获取焦点后，要点击两下才能执行onClick，所以当selected为true时，执行onClick
            if (onClickListener != null) {
                onClickListener.onClick(this);
            }
        } else {
            currentStyle = defStyle;
        }
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                SetPressed(true);
                setOnChildViewFocusChangeListener();
                break;
            case MotionEvent.ACTION_UP:
                SetPressed(false);
                setChecked(!isChecked());
                break;
            case MotionEvent.ACTION_CANCEL:
                SetPressed(false);
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
        setSelected(gainFocus);
    }

    //当this被按下的时候，获取子EditText控件，先取出它的OnFocusChangeListener，然后重新设置监听，在回调里面
    //在调用原来OnFocusChangeListener的onFocusChange
    private void setOnChildViewFocusChangeListener() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof EditText) {
                final OnFocusChangeListener onFocusChangeListener = view.getOnFocusChangeListener();
                view.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        setSelected(hasFocus);
                        if (onFocusChangeListener != null) {
                            onFocusChangeListener.onFocusChange(v, hasFocus);
                        }
                    }
                });
            }
        }
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setSolidColor(@ColorInt int solid_color) {
        currentStyle.solid_color = solid_color;
        currentStyle.solid_start_color = solid_color;
        currentStyle.solid_end_color = solid_color;
        invalidate();
    }

    public void setSolidStartColor(@ColorInt int solid_start_color) {
        currentStyle.solid_start_color = solid_start_color;
        invalidate();
    }

    public void setSolidEndColor(@ColorInt int solid_end_color) {
        currentStyle.solid_end_color = solid_end_color;
        invalidate();
    }

    public void setStrokeColor(@ColorInt int stroke_color) {
        currentStyle.stroke_color = stroke_color;
        currentStyle.stroke_start_color = stroke_color;
        currentStyle.stroke_end_color = stroke_color;
        invalidate();
    }

    public void setStrokeStartColor(@ColorInt int stroke_start_color) {
        currentStyle.stroke_start_color = stroke_start_color;
        invalidate();
    }

    public void setStrokeEndColor(@ColorInt int stroke_end_color) {
        currentStyle.stroke_end_color = stroke_end_color;
        invalidate();
    }

    public void setStrokeWidth(float stroke_width) {
        currentStyle.stroke_width = stroke_width;
        invalidate();
    }

    public void setStrokeDashGap(float stroke_dashGap) {
        currentStyle.stroke_dash_gap = stroke_dashGap;
        invalidate();
    }

    public void setStrokeDashWidth(float stroke_dashWidth) {
        currentStyle.stroke_dash_width = stroke_dashWidth;
        invalidate();
    }

    public void setCornersRadius(float corners_radius) {
        currentStyle.corners_radius = corners_radius;
        currentStyle.corners_radius_left_top = corners_radius;
        currentStyle.corners_radius_right_top = corners_radius;
        currentStyle.corners_radius_left_bottom = corners_radius;
        currentStyle.corners_radius_right_bottom = corners_radius;
        invalidate();
    }

    public void setCornersRadius(float corners_radius_left_top, float corners_radius_right_top, float corners_radius_right_bottom, float corners_radius_left_bottom) {
        currentStyle.corners_radius_left_top = corners_radius_left_top;
        currentStyle.corners_radius_right_top = corners_radius_right_top;
        currentStyle.corners_radius_right_bottom = corners_radius_right_bottom;
        currentStyle.corners_radius_left_bottom = corners_radius_left_bottom;
        invalidate();
    }
}
