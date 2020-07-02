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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gt.utils.GsonUtils;
import com.gt.utils.R;

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

/**
 * 绘制背景控制器
 */
public class ViewBgControl {
    public static int NULLColor = 1;

    public class Style {
        public int solid_color;
        public int solid_start_color;
        public int solid_end_color;
        public int solid_gradual_orientation;
        public int stroke_color;
        public int stroke_start_color;
        public int stroke_end_color;
        public int stroke_gradual_orientation;
        public float stroke_width;
        public float stroke_dash_gap;
        public float stroke_dash_width;
        public float corners_radius;

        public float corners_radius_left_top_x;
        public float corners_radius_left_top_y;
        public float corners_radius_left_bottom_x;
        public float corners_radius_left_bottom_y;
        public float corners_radius_right_top_x;
        public float corners_radius_right_top_y;
        public float corners_radius_right_bottom_x;
        public float corners_radius_right_bottom_y;
    }

    public Style currentStyle, defStyle = new Style(), noEnabledStyle = new Style(), checkedStyle = new Style(), focusedStyle = new Style(), pressedStyle = new Style();
    public boolean checked, focused;

    private View view;

    public ViewBgControl(View view) {
        this.view = view;
    }

    public void initBgStyle(Context context, AttributeSet attrs) {
        initBgStyle(context.obtainStyledAttributes(attrs, R.styleable.BgLayout));
    }

    public void initBgStyle(TypedArray typedArray) {
        defStyle.solid_color = typedArray.getColor(R.styleable.BgLayout_solid_color, Color.TRANSPARENT);
        defStyle.solid_start_color = typedArray.getColor(R.styleable.BgLayout_solid_start_color, defStyle.solid_color);
        defStyle.solid_end_color = typedArray.getColor(R.styleable.BgLayout_solid_end_color, defStyle.solid_color);
        defStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_solid_gradual_orientation, 0);
        defStyle.stroke_color = typedArray.getColor(R.styleable.BgLayout_stroke_color, Color.TRANSPARENT);
        defStyle.stroke_start_color = typedArray.getColor(R.styleable.BgLayout_stroke_start_color, defStyle.stroke_color);
        defStyle.stroke_end_color = typedArray.getColor(R.styleable.BgLayout_stroke_end_color, defStyle.stroke_color);
        defStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_stroke_gradual_orientation, 0);
        defStyle.stroke_width = typedArray.getDimension(R.styleable.BgLayout_stroke_width, 0.0f);
        defStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_gap, 0);
        defStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_width, 0);
        defStyle.corners_radius = typedArray.getDimension(R.styleable.BgLayout_corners_radius, 0);
        defStyle.corners_radius_left_top_x = defStyle.corners_radius_left_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_top, defStyle.corners_radius);
        defStyle.corners_radius_right_top_x = defStyle.corners_radius_right_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_top, defStyle.corners_radius);
        defStyle.corners_radius_left_bottom_x = defStyle.corners_radius_left_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_bottom, defStyle.corners_radius);
        defStyle.corners_radius_right_bottom_x = defStyle.corners_radius_right_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_bottom, defStyle.corners_radius);
        currentStyle = GsonUtils.getGson().fromJson(GsonUtils.getGson().toJson(defStyle), Style.class);

        noEnabledStyle.solid_color = typedArray.getColor(R.styleable.BgLayout_solid_color_no_enabled, NULLColor);
        noEnabledStyle.solid_start_color = typedArray.getColor(R.styleable.BgLayout_solid_start_color_no_enabled, noEnabledStyle.solid_color);
        noEnabledStyle.solid_end_color = typedArray.getColor(R.styleable.BgLayout_solid_end_color_no_enabled, noEnabledStyle.solid_color);
        noEnabledStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_solid_gradual_orientation_no_enabled, -1);
        noEnabledStyle.stroke_color = typedArray.getColor(R.styleable.BgLayout_stroke_color_no_enabled, NULLColor);
        noEnabledStyle.stroke_start_color = typedArray.getColor(R.styleable.BgLayout_stroke_start_color_no_enabled, noEnabledStyle.stroke_color);
        noEnabledStyle.stroke_end_color = typedArray.getColor(R.styleable.BgLayout_stroke_end_color_no_enabled, noEnabledStyle.stroke_color);
        noEnabledStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_stroke_gradual_orientation_no_enabled, -1);
        noEnabledStyle.stroke_width = typedArray.getDimension(R.styleable.BgLayout_stroke_width_no_enabled, -1);
        noEnabledStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_gap_no_enabled, -1);
        noEnabledStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_width_no_enabled, -1);
        noEnabledStyle.corners_radius = typedArray.getDimension(R.styleable.BgLayout_corners_radius_no_enabled, -1);
        noEnabledStyle.corners_radius_left_top_x = noEnabledStyle.corners_radius_left_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_top_no_enabled, noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_right_top_x = noEnabledStyle.corners_radius_right_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_top_no_enabled, noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_left_bottom_x = noEnabledStyle.corners_radius_left_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_bottom_no_enabled, noEnabledStyle.corners_radius);
        noEnabledStyle.corners_radius_right_bottom_x = noEnabledStyle.corners_radius_right_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_bottom_no_enabled, noEnabledStyle.corners_radius);

        checkedStyle.solid_color = typedArray.getColor(R.styleable.BgLayout_solid_color_checked, NULLColor);
        checkedStyle.solid_start_color = typedArray.getColor(R.styleable.BgLayout_solid_start_color_checked, checkedStyle.solid_color);
        checkedStyle.solid_end_color = typedArray.getColor(R.styleable.BgLayout_solid_end_color_checked, checkedStyle.solid_color);
        checkedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_solid_gradual_orientation_checked, -1);
        checkedStyle.stroke_color = typedArray.getColor(R.styleable.BgLayout_stroke_color_checked, NULLColor);
        checkedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgLayout_stroke_start_color_checked, checkedStyle.stroke_color);
        checkedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgLayout_stroke_end_color_checked, checkedStyle.stroke_color);
        checkedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_stroke_gradual_orientation_checked, -1);
        checkedStyle.stroke_width = typedArray.getDimension(R.styleable.BgLayout_stroke_width_checked, -1);
        checkedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_gap_checked, -1);
        checkedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_width_checked, -1);
        checkedStyle.corners_radius = typedArray.getDimension(R.styleable.BgLayout_corners_radius_checked, -1);
        checkedStyle.corners_radius_left_top_x = checkedStyle.corners_radius_left_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_top_checked, checkedStyle.corners_radius);
        checkedStyle.corners_radius_right_top_x = checkedStyle.corners_radius_right_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_top_checked, checkedStyle.corners_radius);
        checkedStyle.corners_radius_left_bottom_x = checkedStyle.corners_radius_left_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_bottom_checked, checkedStyle.corners_radius);
        checkedStyle.corners_radius_right_bottom_x = checkedStyle.corners_radius_right_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_bottom_checked, checkedStyle.corners_radius);

        pressedStyle.solid_color = typedArray.getColor(R.styleable.BgLayout_solid_color_pressed, NULLColor);
        pressedStyle.solid_start_color = typedArray.getColor(R.styleable.BgLayout_solid_start_color_pressed, pressedStyle.solid_color);
        pressedStyle.solid_end_color = typedArray.getColor(R.styleable.BgLayout_solid_end_color_pressed, pressedStyle.solid_color);
        pressedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_solid_gradual_orientation_pressed, -1);
        pressedStyle.stroke_color = typedArray.getColor(R.styleable.BgLayout_stroke_color_pressed, NULLColor);
        pressedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgLayout_stroke_start_color_pressed, pressedStyle.stroke_color);
        pressedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgLayout_stroke_end_color_pressed, pressedStyle.stroke_color);
        pressedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_stroke_gradual_orientation_pressed, -1);
        pressedStyle.stroke_width = typedArray.getDimension(R.styleable.BgLayout_stroke_width_pressed, -1);
        pressedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_gap_pressed, -1);
        pressedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_width_pressed, -1);
        pressedStyle.corners_radius = typedArray.getDimension(R.styleable.BgLayout_corners_radius_pressed, -1);
        pressedStyle.corners_radius_left_top_x = pressedStyle.corners_radius_left_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_top_pressed, pressedStyle.corners_radius);
        pressedStyle.corners_radius_right_top_x = pressedStyle.corners_radius_right_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_top_pressed, pressedStyle.corners_radius);
        pressedStyle.corners_radius_left_bottom_x = pressedStyle.corners_radius_left_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_bottom_pressed, pressedStyle.corners_radius);
        pressedStyle.corners_radius_right_bottom_x = pressedStyle.corners_radius_right_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_bottom_pressed, pressedStyle.corners_radius);

        focusedStyle.solid_color = typedArray.getColor(R.styleable.BgLayout_solid_color_focused, NULLColor);
        focusedStyle.solid_start_color = typedArray.getColor(R.styleable.BgLayout_solid_start_color_focused, focusedStyle.solid_color);
        focusedStyle.solid_end_color = typedArray.getColor(R.styleable.BgLayout_solid_end_color_focused, focusedStyle.solid_color);
        focusedStyle.solid_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_solid_gradual_orientation_focused, -1);
        focusedStyle.stroke_color = typedArray.getColor(R.styleable.BgLayout_stroke_color_focused, NULLColor);
        focusedStyle.stroke_start_color = typedArray.getColor(R.styleable.BgLayout_stroke_start_color_focused, focusedStyle.stroke_color);
        focusedStyle.stroke_end_color = typedArray.getColor(R.styleable.BgLayout_stroke_end_color_focused, focusedStyle.stroke_color);
        focusedStyle.stroke_gradual_orientation = typedArray.getInteger(R.styleable.BgLayout_stroke_gradual_orientation_focused, -1);
        focusedStyle.stroke_width = typedArray.getDimension(R.styleable.BgLayout_stroke_width_focused, -1);
        focusedStyle.stroke_dash_gap = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_gap_focused, -1);
        focusedStyle.stroke_dash_width = typedArray.getDimension(R.styleable.BgLayout_stroke_dash_width_focused, -1);
        focusedStyle.corners_radius = typedArray.getDimension(R.styleable.BgLayout_corners_radius_focused, -1);
        focusedStyle.corners_radius_left_top_x = focusedStyle.corners_radius_left_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_top_focused, focusedStyle.corners_radius);
        focusedStyle.corners_radius_right_top_x = focusedStyle.corners_radius_right_top_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_top_focused, focusedStyle.corners_radius);
        focusedStyle.corners_radius_left_bottom_x = focusedStyle.corners_radius_left_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_left_bottom_focused, focusedStyle.corners_radius);
        focusedStyle.corners_radius_right_bottom_x = focusedStyle.corners_radius_right_bottom_y = typedArray.getDimension(R.styleable.BgLayout_corners_radius_right_bottom_focused, focusedStyle.corners_radius);

        typedArray.recycle();//释放资源
    }

    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Path path = new Path();

        //画边框
        {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(currentStyle.stroke_width);
            int startX = 0, startY = 0, endX = view.getWidth(), endY = view.getHeight();
            if (currentStyle.stroke_gradual_orientation == 0) {
                startY = endY = view.getHeight() / 2;
            } else if (currentStyle.stroke_gradual_orientation == 1) {
                startX = endX = view.getWidth() / 2;
            }
            Shader mShader = new LinearGradient(startX, startY, endX, endY, new int[]{currentStyle.stroke_start_color, currentStyle.stroke_end_color}, null, Shader.TileMode.CLAMP);
            paint.setShader(mShader);

            if (currentStyle.stroke_dash_width > 0)
                paint.setPathEffect(new DashPathEffect(new float[]{currentStyle.stroke_dash_width, currentStyle.stroke_dash_gap}, 0));
            path.addRoundRect(new RectF(currentStyle.stroke_width / 2, currentStyle.stroke_width / 2,
                            view.getWidth() - currentStyle.stroke_width / 2, view.getHeight() - currentStyle.stroke_width / 2),
                    new float[]{currentStyle.corners_radius_left_top_x, currentStyle.corners_radius_left_top_y, currentStyle.corners_radius_right_top_x, currentStyle.corners_radius_right_top_y,
                            currentStyle.corners_radius_right_bottom_x, currentStyle.corners_radius_right_bottom_y, currentStyle.corners_radius_left_bottom_x, currentStyle.corners_radius_left_bottom_y},
                    Path.Direction.CW);
            canvas.drawPath(path, paint);
        }

        //画背景
        {
            paint.setStyle(Paint.Style.FILL);
            //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。
            // 然后那个数组是渐变的颜色。
            // 下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
            int startX = 0, startY = 0, endX = view.getWidth(), endY = view.getHeight();
            if (currentStyle.solid_gradual_orientation == 0) {
                startY = endY = view.getHeight() / 2;
            } else if (currentStyle.solid_gradual_orientation == 1) {
                startX = endX = view.getWidth() / 2;
            }
            Shader mShader = new LinearGradient(startX, startY, endX, endY, new int[]{currentStyle.solid_start_color, currentStyle.solid_end_color}, null, Shader.TileMode.CLAMP);
            paint.setShader(mShader);

            path.addRoundRect(new RectF(currentStyle.stroke_width, currentStyle.stroke_width,
                            view.getWidth() - currentStyle.stroke_width, view.getHeight() - currentStyle.stroke_width),
                    new float[]{currentStyle.corners_radius_left_top_x, currentStyle.corners_radius_left_top_y, currentStyle.corners_radius_right_top_x, currentStyle.corners_radius_right_top_y,
                            currentStyle.corners_radius_right_bottom_x, currentStyle.corners_radius_right_bottom_y, currentStyle.corners_radius_left_bottom_x, currentStyle.corners_radius_left_bottom_y},
                    Path.Direction.CW);
            canvas.drawPath(path, paint);
        }
    }

    public void dispatchTouchEvent(MotionEvent ev) {
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
                if (!ViewUrils.findTopChildUnder(view, ev.getRawX(), ev.getRawY())) {
                    SetPressed(false);
                }
                break;
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            setCurrentStyle(defStyle);
        } else {
            setCurrentStyle(noEnabledStyle);
        }
    }

    public void setChecked(boolean checked) {
        if (view.isEnabled()) {
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
        if (view.isEnabled()) {
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
        if (view.isEnabled()) {
            if (this.focused != focused) {
                this.focused = focused;
                if (this.focused) {
                    //当没有设置focusedStyle样式时，样式不变
                    setCurrentStyle(focusedStyle, currentStyle);
                    // 当设置允许获取焦点后，要点击两下才能执行onClick，所以当selected为true时，执行onClick
                    view.performClick();
                } else {
                    if (this.checked) { //  当该View是选中状态时，失去焦点后设置成checkedStyle
                        setCurrentStyle(checkedStyle);
                    } else
                        setCurrentStyle(defStyle);
                }
            }
        }
    }

    //当该View被按下的时候，获取子EditText控件，先取出它的OnFocusChangeListener，然后重新设置监听，在回调里面
    //在调用原来OnFocusChangeListener的onFocusChange
    private void setOnChildViewFocusChangeListener() {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof TextView) {
                    final View.OnFocusChangeListener onFocusChangeListener = view.getOnFocusChangeListener();
                    view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
    }

    public void setCurrentStyle(Style style) {
        setCurrentStyle(style, defStyle);
    }

    public void setCurrentStyle(Style style, Style defStyle) {
        setCurrentStyle(style, defStyle, this.defStyle);
    }

    public void setCurrentStyle(Style style, Style assistStyle, Style defStyle) {
        if (style.solid_start_color != NULLColor)
            currentStyle.solid_start_color = style.solid_start_color;
        else if (assistStyle.solid_start_color != NULLColor)
            currentStyle.solid_start_color = assistStyle.solid_start_color;
        else
            currentStyle.solid_start_color = defStyle.solid_start_color;

        if (style.solid_end_color != NULLColor)
            currentStyle.solid_end_color = style.solid_end_color;
        else if (assistStyle.solid_end_color != NULLColor)
            currentStyle.solid_end_color = assistStyle.solid_end_color;
        else
            currentStyle.solid_end_color = defStyle.solid_end_color;

        if (style.solid_gradual_orientation != -1)
            currentStyle.solid_gradual_orientation = style.solid_gradual_orientation;
        else if (assistStyle.solid_gradual_orientation != -1)
            currentStyle.solid_gradual_orientation = assistStyle.solid_gradual_orientation;
        else
            currentStyle.solid_gradual_orientation = defStyle.solid_gradual_orientation;

        if (style.stroke_start_color != NULLColor)
            currentStyle.stroke_start_color = style.stroke_start_color;
        else if (assistStyle.stroke_start_color != NULLColor)
            currentStyle.stroke_start_color = assistStyle.stroke_start_color;
        else
            currentStyle.stroke_start_color = defStyle.stroke_start_color;

        if (style.stroke_end_color != NULLColor)
            currentStyle.stroke_end_color = style.stroke_end_color;
        else if (assistStyle.stroke_end_color != NULLColor)
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

        if (style.corners_radius_left_top_x != -1)
            currentStyle.corners_radius_left_top_x = style.corners_radius_left_top_x;
        else if (assistStyle.corners_radius_left_top_x != -1)
            currentStyle.corners_radius_left_top_x = assistStyle.corners_radius_left_top_x;
        else
            currentStyle.corners_radius_left_top_x = defStyle.corners_radius_left_top_x;

        if (style.corners_radius_left_top_y != -1)
            currentStyle.corners_radius_left_top_y = style.corners_radius_left_top_y;
        else if (assistStyle.corners_radius_left_top_y != -1)
            currentStyle.corners_radius_left_top_y = assistStyle.corners_radius_left_top_y;
        else
            currentStyle.corners_radius_left_top_y = defStyle.corners_radius_left_top_y;

        if (style.corners_radius_right_top_x != -1)
            currentStyle.corners_radius_right_top_x = style.corners_radius_right_top_x;
        else if (assistStyle.corners_radius_right_top_x != -1)
            currentStyle.corners_radius_right_top_x = assistStyle.corners_radius_right_top_x;
        else
            currentStyle.corners_radius_right_top_x = defStyle.corners_radius_right_top_x;

        if (style.corners_radius_right_top_y != -1)
            currentStyle.corners_radius_right_top_y = style.corners_radius_right_top_y;
        else if (assistStyle.corners_radius_right_top_y != -1)
            currentStyle.corners_radius_right_top_y = assistStyle.corners_radius_right_top_y;
        else
            currentStyle.corners_radius_right_top_y = defStyle.corners_radius_right_top_y;

        if (style.corners_radius_left_bottom_x != -1)
            currentStyle.corners_radius_left_bottom_x = style.corners_radius_left_bottom_x;
        else if (assistStyle.corners_radius_left_bottom_x != -1)
            currentStyle.corners_radius_left_bottom_x = assistStyle.corners_radius_left_bottom_x;
        else
            currentStyle.corners_radius_left_bottom_x = defStyle.corners_radius_left_bottom_x;

        if (style.corners_radius_left_bottom_y != -1)
            currentStyle.corners_radius_left_bottom_y = style.corners_radius_left_bottom_y;
        else if (assistStyle.corners_radius_left_bottom_y != -1)
            currentStyle.corners_radius_left_bottom_y = assistStyle.corners_radius_left_bottom_y;
        else
            currentStyle.corners_radius_left_bottom_y = defStyle.corners_radius_left_bottom_y;

        if (style.corners_radius_right_bottom_x != -1)
            currentStyle.corners_radius_right_bottom_x = style.corners_radius_right_bottom_x;
        else if (assistStyle.corners_radius_right_bottom_x != -1)
            currentStyle.corners_radius_right_bottom_x = assistStyle.corners_radius_right_bottom_x;
        else
            currentStyle.corners_radius_right_bottom_x = defStyle.corners_radius_right_bottom_x;

        if (style.corners_radius_right_bottom_y != -1)
            currentStyle.corners_radius_right_bottom_y = style.corners_radius_right_bottom_y;
        else if (assistStyle.corners_radius_right_bottom_y != -1)
            currentStyle.corners_radius_right_bottom_y = assistStyle.corners_radius_right_bottom_y;
        else
            currentStyle.corners_radius_right_bottom_y = defStyle.corners_radius_right_bottom_y;

        view.invalidate();
    }
}
