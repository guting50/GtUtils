package com.gt.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.gt.utils.R;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;


/**
 * 自定义可设置背景的帧布局
 * 自定义属性
 * <declare-styleable name="MyTextView">
 * <attr name="solid_color" format="color" /><!--填充色-->
 * <attr name="solid_start_color" format="color" /><!--填充渐变开始颜色-->
 * <attr name="solid_end_color" format="color" /><!--填充渐变结束颜色-->
 * <attr name="solid_gradual_change_orientation" format="enum"><!--填充渐变方向，默认diagonal-->
 * <enum name="horizontal" value="0" />
 * <enum name="vertical" value="1" />
 * <enum name="diagonal" value="-1" />
 * </attr>
 * <attr name="stroke_color" format="color" /><!--边框颜色-->
 * <attr name="stroke_start_color" format="color" /><!--边框渐变开始颜色-->
 * <attr name="stroke_end_color" format="color" /><!--边框渐变结束颜色-->
 * <attr name="stroke_gradual_change_orientation" format="enum"><!--边框渐变方向，默认diagonal-->
 * <enum name="horizontal" value="0" />
 * <enum name="vertical" value="1" />
 * <enum name="diagonal" value="-1" />
 * </attr>
 * <attr name="stroke_width" format="dimension" /><!--边框宽度-->
 * <attr name="stroke_dash_gap" format="dimension" /><!--间隔-->
 * <attr name="stroke_dash_width" format="dimension" /><!--点的大小-->
 * <attr name="corners_radius" format="dimension" /><!--圆角弧度-->
 * <attr name="corners_radius_left_top" format="dimension" /><!--圆角弧度 左上角-->
 * <attr name="corners_radius_right_top" format="dimension" /><!--圆角弧度 右上角-->
 * <attr name="corners_radius_left_bottom" format="dimension" /><!--圆角弧度 左下角-->
 * <attr name="corners_radius_right_bottom" format="dimension" /><!--圆角弧度 右下角-->
 * </declare-styleable>
 */
public class BgFrameLayout extends FrameLayout {
    private int solid_color;
    private int solid_start_color;
    private int solid_end_color;
    private int solid_gradual_change_orientation;
    private int stroke_color;
    private int stroke_start_color;
    private int stroke_end_color;
    private int stroke_gradual_change_orientation;
    private float stroke_width;
    private float stroke_dash_gap;
    private float stroke_dash_width;
    private float corners_radius;
    private float corners_radius_left_top;
    private float corners_radius_right_top;
    private float corners_radius_left_bottom;
    private float corners_radius_right_bottom;

    public BgFrameLayout(Context context) {
        super(context);
    }

    public BgFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BgFrameLayout);
        solid_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_color, Color.TRANSPARENT);
        solid_start_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_start_color, solid_color);
        solid_end_color = typedArray.getColor(R.styleable.BgFrameLayout_solid_end_color, solid_color);
        solid_gradual_change_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_solid_gradual_change_orientation, -1);
        stroke_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_color, Color.TRANSPARENT);
        stroke_start_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_start_color, stroke_color);
        stroke_end_color = typedArray.getColor(R.styleable.BgFrameLayout_stroke_end_color, stroke_color);
        stroke_gradual_change_orientation = typedArray.getInteger(R.styleable.BgFrameLayout_stroke_gradual_change_orientation, -1);
        stroke_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_width, 0.0f);
        stroke_dash_gap = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_gap, 0);
        stroke_dash_width = typedArray.getDimension(R.styleable.BgFrameLayout_stroke_dash_width, 0);
        corners_radius = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius, 0);
        corners_radius_left_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_top, corners_radius);
        corners_radius_right_top = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_top, corners_radius);
        corners_radius_left_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_left_bottom, corners_radius);
        corners_radius_right_bottom = typedArray.getDimension(R.styleable.BgFrameLayout_corners_radius_right_bottom, corners_radius);
        typedArray.recycle();//释放资源
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Path path = new Path();
        float strokeWidth = stroke_width * 2;

        //画边框
        {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            int startX = 0, startY = 0, endX = this.getWidth(), endY = this.getHeight();
            if (stroke_gradual_change_orientation == 0) {
                startY = endY = this.getHeight() / 2;
            } else if (stroke_gradual_change_orientation == 1) {
                startX = endX = this.getWidth() / 2;
            }
            Shader mShader = new LinearGradient(startX, startY, endX, endY, new int[]{stroke_start_color, stroke_end_color}, null, Shader.TileMode.CLAMP);
            paint.setShader(mShader);

            if (stroke_dash_width > 0)
                paint.setPathEffect(new DashPathEffect(new float[]{stroke_dash_width, stroke_dash_gap}, 0));
            path.addRoundRect(new RectF(strokeWidth / 2, strokeWidth / 2, this.getWidth() - strokeWidth / 2, this.getHeight() - strokeWidth / 2),
                    new float[]{corners_radius_left_top, corners_radius_left_top, corners_radius_right_top, corners_radius_right_top,
                            corners_radius_right_bottom, corners_radius_right_bottom, corners_radius_left_bottom, corners_radius_left_bottom},
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
            if (solid_gradual_change_orientation == 0) {
                startY = endY = this.getHeight() / 2;
            } else if (solid_gradual_change_orientation == 1) {
                startX = endX = this.getWidth() / 2;
            }
            Shader mShader = new LinearGradient(startX, startY, endX, endY, new int[]{solid_start_color, solid_end_color}, null, Shader.TileMode.CLAMP);
            paint.setShader(mShader);

            //该方法5.0以上才支持
//        path.addRoundRect(strokeWidth * 2, strokeWidth * 2, this.getWidth() - strokeWidth * 2, this.getHeight() - strokeWidth * 2,
//                corners_radius, corners_radius, Path.Direction.CW);
            path.addRoundRect(new RectF(strokeWidth, strokeWidth, this.getWidth() - strokeWidth, this.getHeight() - strokeWidth),
                    new float[]{corners_radius_left_top, corners_radius_left_top, corners_radius_right_top, corners_radius_right_top,
                            corners_radius_right_bottom, corners_radius_right_bottom, corners_radius_left_bottom, corners_radius_left_bottom},
                    Path.Direction.CW);
            canvas.drawPath(path, paint);
        }
        super.dispatchDraw(canvas);
    }

    public void setSolidColor(@ColorInt int solid_color) {
        this.solid_start_color = solid_color;
        this.solid_end_color = solid_color;
        invalidate();
    }

    public void setSolidStartColor(@ColorInt int solid_start_color) {
        this.solid_start_color = solid_start_color;
        invalidate();
    }

    public void setSolidEndColor(@ColorInt int solid_end_color) {
        this.solid_end_color = solid_end_color;
        invalidate();
    }

    public void setStrokeColor(@ColorInt int stroke_color) {
        this.stroke_start_color = stroke_color;
        this.stroke_end_color = stroke_color;
        invalidate();
    }

    public void setStrokeStartColor(@ColorInt int stroke_start_color) {
        this.stroke_start_color = stroke_start_color;
        invalidate();
    }

    public void setStrokeEndColor(@ColorInt int stroke_end_color) {
        this.stroke_end_color = stroke_end_color;
        invalidate();
    }

    public void setStrokeWidth(float stroke_width) {
        this.stroke_width = stroke_width;
        invalidate();
    }

    public void setStrokeDashGap(float stroke_dashGap) {
        this.stroke_dash_gap = stroke_dashGap;
        invalidate();
    }

    public void setStrokeDashWidth(float stroke_dashWidth) {
        this.stroke_dash_width = stroke_dashWidth;
        invalidate();
    }

    public void setCornersRadius(float corners_radius) {
        this.corners_radius = corners_radius;
        this.corners_radius_left_top = this.corners_radius;
        this.corners_radius_right_top = this.corners_radius;
        this.corners_radius_left_bottom = this.corners_radius;
        this.corners_radius_right_bottom = this.corners_radius;
        invalidate();
    }

    public void setCornersRadius(float corners_radius_left_top, float corners_radius_right_top, float corners_radius_right_bottom, float corners_radius_left_bottom) {
        this.corners_radius_left_top = corners_radius_left_top;
        this.corners_radius_right_top = corners_radius_right_top;
        this.corners_radius_right_bottom = corners_radius_right_bottom;
        this.corners_radius_left_bottom = corners_radius_left_bottom;
        invalidate();
    }
}
