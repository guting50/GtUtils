package com.gt.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gt.utils.R;

import java.util.LinkedList;

/**
 * 自定义手写板控件
 * 自定义属性
 * <attr name="paint_color" format="color" /><!--画笔的颜色-->
 * <attr name="paint_stroke_width" format="float" /><!--画笔的宽度-->
 * <attr name="paint_type" format="enum"> <!--画笔的类型-->
 * ---<enum name="curve" value="0" /><!--曲线-->
 * ---<enum name="line" value="1" /><!--直线-->
 * ---<enum name="rect" value="2" /><!--矩形-->
 * ---<enum name="circle" value="3" /><!--圆形-->
 * ---<enum name="oval" value="4" /><!--椭圆形-->
 * </attr>
 */
public class PaintView extends View {
    private Paint mPaint;
    private Path mPath;
    private Path eraserPath;
    private Paint eraserPaint;
    private float mLastX, mLastY;//上次的坐标
    //使用LinkedList 模拟栈，来保存 Path
    private LinkedList<PathBean> undoList;
    private LinkedList<PathBean> redoList;
    private boolean isEraserModel;
    private OnCurrentDrawListener onCurrentDrawListener;
    private boolean prohibitTouch = false;
    private int type = 0;//0：根据触摸点画，1：画直线，2：画矩形，3：画圆，4：椭圆
    private boolean isDraw = true;


    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BgFrameLayout);
        mPaint.setColor(typedArray.getColor(R.styleable.PaintView_paint_color, Color.RED));
        mPaint.setStrokeWidth(typedArray.getFloat(R.styleable.PaintView_paint_stroke_width, 4f));
        type = typedArray.getInteger(R.styleable.PaintView_paint_type, 0);
        typedArray.recycle();//释放资源
    }

    /***
     * 初始化
     */
    private void init() {
        //关闭硬件加速
        //否则橡皮擦模式下，设置的 PorterDuff.Mode.CLEAR ，实时绘制的轨迹是黑色
        setBackgroundColor(Color.TRANSPARENT);//设置背景为透明
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(4f);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);//使画笔更加圆润
        mPaint.setStrokeCap(Paint.Cap.ROUND);//同上

        undoList = new LinkedList<>();
        redoList = new LinkedList<>();
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDraw) {
            if (!isEraserModel) {
                if (mPath != null) {
                    canvas.drawPath(mPath, mPaint);
                }
                for (int i = 0; i < undoList.size(); i++) {
                    PathBean pathBean = undoList.get(i);
                    Path path = pathBean.path;
                    Paint paint = pathBean.paint;
                    if (null != path) {//显示实时正在绘制的path轨迹
                        canvas.drawPath(path, paint);
                    }
                }
            } else {
                if (null != eraserPath) {
                    canvas.drawPath(eraserPath, eraserPaint);
                }
            }
        }

        if (onCurrentDrawListener != null) {
            onCurrentDrawListener.onCurrentDraw();
        }
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    /**
     * 撤销操作
     */
    public void undo() {
        if (!undoList.isEmpty()) {
            PathBean lastPb = undoList.removeLast();//将最后一个移除
            redoList.add(lastPb);//加入 恢复操作
            invalidate();
        }
    }

    /**
     * 恢复操作
     */
    public void redo() {
        if (!redoList.isEmpty()) {
            PathBean pathBean = redoList.removeLast();
            undoList.add(pathBean);
            invalidate();
        }
    }

    /**
     * 设置画笔颜色
     */
    public void setPaintColor(@ColorInt int color) {
        if (mPaint != null)
            mPaint.setColor(color);
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 清空，包括撤销和恢复操作列表
     */
    public void clearAll() {
        clearPaint();
        mLastY = 0f;
    }

    /**
     * 设置橡皮擦模式
     */
    public void setEraserModel(boolean isEraserModel) {
        this.isEraserModel = isEraserModel;
        if (eraserPaint == null) {
            eraserPaint = new Paint(mPaint);
            eraserPaint.setStrokeWidth(15f);
            eraserPaint.setColor(Color.TRANSPARENT);
            eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
    }

    /**
     * 是否可以撤销
     */
    public boolean isCanUndo() {
        return undoList.isEmpty();
    }

    /**
     * 是否可以恢复
     */
    public boolean isCanRedo() {
        return redoList.isEmpty();
    }

    /**
     * 清除绘制内容
     * 直接绘制白色背景
     */
    private void clearPaint() {
        //清空 撤销 ，恢复 操作列表
        redoList.clear();
        undoList.clear();
        invalidate();
    }

    /**
     * 触摸事件 触摸绘制
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (prohibitTouch) {
            return false;
        }
        if (!isEraserModel) {
            commonTouchEvent(event);
        } else {
            eraserTouchEvent(event);
        }
        invalidate();
        return true;
    }

    /**
     * 橡皮擦事件
     */
    private void eraserTouchEvent(MotionEvent event) {
        int action = event.getAction();
        eraserTouchEvent(action, event.getX(), event.getY());
    }

    private void eraserTouchEvent(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //路径
                eraserPath = new Path();
                mLastX = x;
                mLastY = y;
                eraserPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mLastX);
                float dy = Math.abs(y - mLastY);
                if (dx >= 3 || dy >= 3) {//绘制的最小距离 3px
                    eraserPath.quadTo(mLastX, mLastY, (mLastX + x) / 2, (mLastY + y) / 2);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                eraserPath.reset();
                eraserPath = null;
                break;
        }
    }

    /**
     * 普通画笔事件
     */
    private void commonTouchEvent(MotionEvent event) {
        int action = event.getAction();
        commonTouchEvent(action, event.getX(), event.getY());
    }

    private void commonTouchEvent(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                //路径
                mPath = new Path();
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (type > 0) {
                    mPath.reset();
                    mPath = new Path();
                    mPath.moveTo(mLastX, mLastY);
                }
                float dx = Math.abs(x - mLastX);
                float dy = Math.abs(y - mLastY);
                //起点和终点构成了一个矩形，计算出该矩形左上角和右下角的坐标。
                float leftTopX = 0, leftTopY = 0, rightBottomX = 0, rightBottomY = 0;
                if (x > mLastX) {
                    leftTopX = mLastX;
                    rightBottomX = x;
                } else {
                    leftTopX = x;
                    rightBottomX = mLastX;
                }
                if (y > mLastY) {
                    leftTopY = mLastY;
                    rightBottomY = y;
                } else {
                    leftTopY = y;
                    rightBottomY = mLastY;
                }
                switch (type) {
                    case 0:
                        if (dx >= 3 || dy >= 3) {//绘制的最小距离 3px
                            //利用二阶贝塞尔曲线，使绘制路径更加圆滑
                            mPath.quadTo(mLastX, mLastY, (mLastX + x) / 2, (mLastY + y) / 2);
                        }
                        mLastX = x;
                        mLastY = y;
                        break;
                    case 1:
                        mPath.lineTo(x, y);
                        break;
                    case 2:
                        mPath.addRect(new RectF(leftTopX, leftTopY, rightBottomX, rightBottomY), Path.Direction.CCW);
                        break;
                    case 3:
                        mPath.addCircle(mLastX, mLastY, (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)), Path.Direction.CCW);
                        break;
                    case 4:
                        mPath.addOval(new RectF(leftTopX, leftTopY, rightBottomX, rightBottomY), Path.Direction.CCW);
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                PathBean pb = new PathBean(new Path(mPath), new Paint(mPaint));
                undoList.add(pb);//将路径对象存入集合
                mPath.reset();
                break;
        }
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wSpecMode == MeasureSpec.EXACTLY && hSpecMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, hSpecSize);
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSpecSize, 200);
        }
    }

    public void setProhibitTouch(boolean prohibitTouch) {
        this.prohibitTouch = prohibitTouch;
    }

    public void setOnCurrentDrawListener(OnCurrentDrawListener onCurrentDrawListener) {
        this.onCurrentDrawListener = onCurrentDrawListener;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void addPath(Path path) {
        PathBean pb = new PathBean(new Path(path), new Paint(mPaint));
        undoList.add(pb);
        invalidate();
    }

    /**
     * 路径对象
     */
    public class PathBean {
        public Path path;
        public Paint paint;

        PathBean(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }

    public interface OnCurrentDrawListener {
        void onCurrentDraw();
    }

    /**
     * 注意多个createBiamap重载函数，必须是可变位图对应的重载才能绘制
     *
     * @param bitmap 原图像
     * @return
     */
    public Bitmap drawBackground(Bitmap bitmap) {
        Log.e("drawBackground", " 应用程序最大可用内存=" + (((int) Runtime.getRuntime().maxMemory()) / 1024 / 1024)
                + "M,应用程序已获得内存=" + (((int) Runtime.getRuntime().totalMemory()) / 1024 / 1024)
                + "M,应用程序已获得内存中未使用内存=" + (((int) Runtime.getRuntime().freeMemory()) / 1024 / 1024) + "M");

        if (bitmap != null && !bitmap.isRecycled()) {
            int width = getWidth(), height = getHeight();
            Bitmap copy = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  //很重要
            Canvas canvas = new Canvas(copy);  //创建画布

            int bmpWidth = bitmap.getWidth(), bmpHeight = bitmap.getHeight();
            if ((double) width / (double) height >= (double) bmpWidth / (double) bmpHeight) {
                bmpHeight = bmpWidth * height / width;
            } else {
                bmpWidth = bmpHeight * width / height;
            }
            Rect mTopSrcRect = new Rect(0, 0, bmpWidth, bmpHeight);
            Rect mTopDestRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(bitmap, mTopSrcRect, mTopDestRect, mPaint);

            if (mPath != null) {//显示实时正在绘制的path轨迹
                canvas.drawPath(mPath, mPaint);
            }
            for (int i = 0; i < undoList.size(); i++) {
                PathBean pathBean = undoList.get(i);
                Path path = pathBean.path;
                Paint paint = pathBean.paint;
                if (null != path) {
                    canvas.drawPath(path, paint);
                }
            }
            return copy;
        }
        return null;
    }
}
