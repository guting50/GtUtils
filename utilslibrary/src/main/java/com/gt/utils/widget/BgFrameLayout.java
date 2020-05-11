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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gt.utils.GsonUtils;
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

        <!--focused-->
        <attr name="solid_color_focused" format="color" /><!--填充色-->
        <attr name="solid_start_color_focused" format="color" /><!--填充渐变开始颜色-->
        <attr name="solid_end_color_focused" format="color" /><!--填充渐变结束颜色-->
        <attr name="solid_gradual_orientation_focused" format="enum"><!--填充渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_color_focused" format="color" /><!--边框颜色-->
        <attr name="stroke_start_color_focused" format="color" /><!--边框渐变开始颜色-->
        <attr name="stroke_end_color_focused" format="color" /><!--边框渐变结束颜色-->
        <attr name="stroke_gradual_orientation_focused" format="enum"><!--边框渐变方向，默认horizontal-->
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
            <enum name="diagonal" value="-1" />
        </attr>
        <attr name="stroke_width_focused" format="dimension" /><!--边框宽度-->
        <attr name="stroke_dash_gap_focused" format="dimension" /><!--间隔-->
        <attr name="stroke_dash_width_focused" format="dimension" /><!--点的大小-->
        <attr name="corners_radius_focused" format="dimension" /><!--圆角弧度-->
        <attr name="corners_radius_left_top_focused" format="dimension" /><!--圆角弧度 左上角-->
        <attr name="corners_radius_right_top_focused" format="dimension" /><!--圆角弧度 右上角-->
        <attr name="corners_radius_left_bottom_focused" format="dimension" /><!--圆角弧度 左下角-->
        <attr name="corners_radius_right_bottom_focused" format="dimension" /><!--圆角弧度 右下角-->
 */

/*
 * 获取焦点 需要设置android:focusableInTouchMode="true"
 * 不要用这个来模拟单选，它和单选的逻辑不一样，全局只能有一个view可以获取焦点
 * （当子控件获取焦点的时候，它就获取不到了，所以这里是模拟它获取到了，
 * 应用场景是给获取到焦点的控件设置Style，比如给获取到焦点的文本输入框加背景）
 *
 * 当该view按下时，显示pressedStyle
 * 抬起时，如果focused == true ，显示focusedStyle
 *         如果checked == true，显示checkedStyle
 *         否者显示defStyle
 *
 * 选中时，如果focused == true，显示focusedStyle
 *         如果checked == true，显示checkedStyle
 *         否者显示defStyle
 *
 * 获取到焦点时，显示focusedStyle
 * 失去焦点时，如果checked == true，显示checkedStyle
 *             否者显示defStyle
 *
 * focusedStyle 和 checkedStyle 尽量不要同时使用，容易混淆
 *
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

    public Style currentStyle, defStyle = new Style(), noEnabledStyle = new Style(), checkedStyle = new Style(), focusedStyle = new Style(), pressedStyle = new Style();
    private boolean checked, focused;
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
        currentStyle = GsonUtils.getGson().fromJson(GsonUtils.getGson().toJson(defStyle), Style.class);

        noEnabledStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color_no_enabled, -1);
        noEnabledStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color_no_enabled, noEnabledStyle.solid_color);
        noEnabledStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color_no_enabled, noEnabledStyle.solid_color);
        noEnabledStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation_no_enabled, -1);
        noEnabledStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color_no_enabled, -1);
        noEnabledStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color_no_enabled, noEnabledStyle.stroke_color);
        noEnabledStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color_no_enabled, noEnabledStyle.stroke_color);
        noEnabledStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation_no_enabled, -1);
        noEnabledStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width_no_enabled, -1);
        noEnabledStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap_no_enabled, -1);
        noEnabledStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width_no_enabled, -1);
        noEnabledStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_no_enabled, -1);
        noEnabledStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top_no_enabled, noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top_no_enabled, noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom_no_enabled, noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom_no_enabled, noEnabledStyle.corners_radius);

        checkedStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color_checked, -1);
        checkedStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color_checked, checkedStyle.solid_color);
        checkedStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color_checked, checkedStyle.solid_color);
        checkedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation_checked, -1);
        checkedStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color_checked, -1);
        checkedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color_checked, checkedStyle.stroke_color);
        checkedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color_checked, checkedStyle.stroke_color);
        checkedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation_checked, -1);
        checkedStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width_checked, -1);
        checkedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap_checked, -1);
        checkedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width_checked, -1);
        checkedStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_checked, -1);
        checkedStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top_checked, checkedStyle.corners_radius);
        checkedStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top_checked, checkedStyle.corners_radius);
        checkedStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom_checked, checkedStyle.corners_radius);
        checkedStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom_checked, checkedStyle.corners_radius);

        pressedStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color_pressed, -1);
        pressedStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color_pressed, pressedStyle.solid_color);
        pressedStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color_pressed, pressedStyle.solid_color);
        pressedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation_pressed, -1);
        pressedStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color_pressed, -1);
        pressedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color_pressed, pressedStyle.stroke_color);
        pressedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color_pressed, pressedStyle.stroke_color);
        pressedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation_pressed, -1);
        pressedStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width_pressed, -1);
        pressedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap_pressed, -1);
        pressedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width_pressed, -1);
        pressedStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_pressed, -1);
        pressedStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top_pressed, pressedStyle.corners_radius);
        pressedStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top_pressed, pressedStyle.corners_radius);
        pressedStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom_pressed, pressedStyle.corners_radius);
        pressedStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom_pressed, pressedStyle.corners_radius);

        focusedStyle.solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color_focused, -1);
        focusedStyle.solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color_focused, focusedStyle.solid_color);
        focusedStyle.solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color_focused, focusedStyle.solid_color);
        focusedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_orientation_focused, -1);
        focusedStyle.stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color_focused, -1);
        focusedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color_focused, focusedStyle.stroke_color);
        focusedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color_focused, focusedStyle.stroke_color);
        focusedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_orientation_focused, -1);
        focusedStyle.stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width_focused, -1);
        focusedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap_focused, -1);
        focusedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width_focused, -1);
        focusedStyle.corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_focused, -1);
        focusedStyle.corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top_focused, focusedStyle.corners_radius);
        focusedStyle.corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top_focused, focusedStyle.corners_radius);
        focusedStyle.corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom_focused, focusedStyle.corners_radius);
        focusedStyle.corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom_focused, focusedStyle.corners_radius);

        typedArray.recycle();//释放资源

//        setFocusableInTouchMode(true);

        //当设置在触摸模式下可以获取焦点时，如果不设置点击事件，onFocusChanged不回调
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBgClick(v);
            }
        });
    }

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
                setOnChildViewFocusChangeListener();
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

    //当该View被按下的时候，获取子EditText控件，先取出它的OnFocusChangeListener，然后重新设置监听，在回调里面
    //在调用原来OnFocusChangeListener的onFocusChange
    private void setOnChildViewFocusChangeListener() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                final OnFocusChangeListener onFocusChangeListener = view.getOnFocusChangeListener();
                view.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        setFocused(hasFocus);
                        if (onFocusChangeListener != null) {
                            onFocusChangeListener.onFocusChange(v, hasFocus);
                        }
                    }
                });
            }
        }
    }

    private void onBgClick(View view) {
        setChecked(!checked);
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
    }

    public void setCurrentStyle(Style style) {
        setCurrentStyle(style, defStyle);
    }

    public void setCurrentStyle(Style style, Style defStyle) {
        setCurrentStyle(style, defStyle, this.defStyle);
    }

    public void setCurrentStyle(Style style, Style assistStyle, Style defStyle) {
        if (style.solid_start_color != -1)
            currentStyle.solid_start_color = style.solid_start_color;
        else if (assistStyle.solid_start_color != -1)
            currentStyle.solid_start_color = assistStyle.solid_start_color;
        else
            currentStyle.solid_start_color = defStyle.solid_start_color;

        if (style.solid_end_color != -1)
            currentStyle.solid_end_color = style.solid_end_color;
        else if (assistStyle.solid_end_color != -1)
            currentStyle.solid_end_color = assistStyle.solid_end_color;
        else
            currentStyle.solid_end_color = defStyle.solid_end_color;

        if (style.solid_gradual_orientation != -1)
            currentStyle.solid_gradual_orientation = style.solid_gradual_orientation;
        else if (assistStyle.solid_gradual_orientation != -1)
            currentStyle.solid_gradual_orientation = assistStyle.solid_gradual_orientation;
        else
            currentStyle.solid_gradual_orientation = defStyle.solid_gradual_orientation;

        if (style.stroke_start_color != -1)
            currentStyle.stroke_start_color = style.stroke_start_color;
        else if (assistStyle.stroke_start_color != -1)
            currentStyle.stroke_start_color = assistStyle.stroke_start_color;
        else
            currentStyle.stroke_start_color = defStyle.stroke_start_color;

        if (style.stroke_end_color != -1)
            currentStyle.stroke_end_color = style.stroke_end_color;
        else if (assistStyle.stroke_end_color != -1)
            currentStyle.stroke_end_color = assistStyle.stroke_end_color;
        else
            currentStyle.stroke_end_color = defStyle.stroke_end_color;

        if (style.stroke_gradual_orientation != -1)
            currentStyle.stroke_gradual_orientation = style.stroke_gradual_orientation;
        else if (assistStyle.stroke_gradual_orientation != -1)
            currentStyle.stroke_gradual_orientation = assistStyle.stroke_gradual_orientation;
        else
            currentStyle.stroke_gradual_orientation = defStyle.stroke_gradual_orientation;

        if (style.stroke_width != -1)
            currentStyle.stroke_width = style.stroke_width;
        else if (assistStyle.stroke_width != -1)
            currentStyle.stroke_width = assistStyle.stroke_width;
        else
            currentStyle.stroke_width = defStyle.stroke_width;

        if (style.stroke_dash_gap != -1)
            currentStyle.stroke_dash_gap = style.stroke_dash_gap;
        else if (assistStyle.stroke_dash_gap != -1)
            currentStyle.stroke_dash_gap = assistStyle.stroke_dash_gap;
        else
            currentStyle.stroke_dash_gap = defStyle.stroke_dash_gap;

        if (style.stroke_dash_width != -1)
            currentStyle.stroke_dash_width = style.stroke_dash_width;
        else if (assistStyle.stroke_dash_width != -1)
            currentStyle.stroke_dash_width = assistStyle.stroke_dash_width;
        else
            currentStyle.stroke_dash_width = defStyle.stroke_dash_width;

        if (style.corners_radius_left_top != -1)
            currentStyle.corners_radius_left_top = style.corners_radius_left_top;
        else if (assistStyle.corners_radius_left_top != -1)
            currentStyle.corners_radius_left_top = assistStyle.corners_radius_left_top;
        else
            currentStyle.corners_radius_left_top = defStyle.corners_radius_left_top;

        if (style.corners_radius_right_top != -1)
            currentStyle.corners_radius_right_top = style.corners_radius_right_top;
        else if (assistStyle.corners_radius_right_top != -1)
            currentStyle.corners_radius_right_top = assistStyle.corners_radius_right_top;
        else
            currentStyle.corners_radius_right_top = defStyle.corners_radius_right_top;

        if (style.corners_radius_left_bottom != -1)
            currentStyle.corners_radius_left_bottom = style.corners_radius_left_bottom;
        else if (assistStyle.corners_radius_left_bottom != -1)
            currentStyle.corners_radius_left_bottom = assistStyle.corners_radius_left_bottom;
        else
            currentStyle.corners_radius_left_bottom = defStyle.corners_radius_left_bottom;

        if (style.corners_radius_right_bottom != -1)
            currentStyle.corners_radius_right_bottom = style.corners_radius_right_bottom;
        else if (assistStyle.corners_radius_right_bottom != -1)
            currentStyle.corners_radius_right_bottom = assistStyle.corners_radius_right_bottom;
        else
            currentStyle.corners_radius_right_bottom = defStyle.corners_radius_right_bottom;


        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setSolidColor(@ColorInt int solid_color) {
        currentStyle.solid_color = solid_color;
        currentStyle.solid_start_color = solid_color;
        currentStyle.solid_end_color = solid_color;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setSolidStartColor(@ColorInt int solid_start_color) {
        currentStyle.solid_start_color = solid_start_color;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setSolidEndColor(@ColorInt int solid_end_color) {
        currentStyle.solid_end_color = solid_end_color;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setStrokeColor(@ColorInt int stroke_color) {
        currentStyle.stroke_color = stroke_color;
        currentStyle.stroke_start_color = stroke_color;
        currentStyle.stroke_end_color = stroke_color;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setStrokeStartColor(@ColorInt int stroke_start_color) {
        currentStyle.stroke_start_color = stroke_start_color;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setStrokeEndColor(@ColorInt int stroke_end_color) {
        currentStyle.stroke_end_color = stroke_end_color;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setStrokeWidth(float stroke_width) {
        currentStyle.stroke_width = stroke_width;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setStrokeDashGap(float stroke_dashGap) {
        currentStyle.stroke_dash_gap = stroke_dashGap;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setStrokeDashWidth(float stroke_dashWidth) {
        currentStyle.stroke_dash_width = stroke_dashWidth;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setCornersRadius(float corners_radius) {
        currentStyle.corners_radius = corners_radius;
        currentStyle.corners_radius_left_top = corners_radius;
        currentStyle.corners_radius_right_top = corners_radius;
        currentStyle.corners_radius_left_bottom = corners_radius;
        currentStyle.corners_radius_right_bottom = corners_radius;
        invalidate();
    }

    /**
     * 建议使用setCurrentStyle()
     */
    @Deprecated
    public void setCornersRadius(float corners_radius_left_top, float corners_radius_right_top, float corners_radius_right_bottom, float corners_radius_left_bottom) {
        currentStyle.corners_radius_left_top = corners_radius_left_top;
        currentStyle.corners_radius_right_top = corners_radius_right_top;
        currentStyle.corners_radius_right_bottom = corners_radius_right_bottom;
        currentStyle.corners_radius_left_bottom = corners_radius_left_bottom;
        invalidate();
    }
}
