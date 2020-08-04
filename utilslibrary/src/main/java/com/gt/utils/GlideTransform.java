package com.gt.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Glide4.x 有自带的方法
 * <p>
 * Glide.with(context).load(url)
 * .transform(new CircleCrop())
 * .into(imageView);
 * <p>
 * Glide.with(context).load(url)
 * .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
 * .into(imageView);
 */
@Deprecated
public class GlideTransform {
    /**
     * 下载的图片转圆角的方法
     * Glide.with(context)
     * .load("https://www.baidu.com/img/bdlogo.png")
     * .transform(new GlideCornersTransform(context))
     * .into(imageView);
     * Created by Administrator on 2016/12/28.
     */
    /*public static class GlideCornersTransform extends BitmapTransformation {

        private float radius;

        public GlideCornersTransform(Context context) {
            this(context, 10);
        }

        public GlideCornersTransform(Context context, float radius) {
            super(context);
            this.radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, context.getResources().getDisplayMetrics());
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return cornersCrop(pool, toTransform);
        }

        private Bitmap cornersCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    *//**
     * 下载的图片转圆形的方法
     * Glide.with(context)
     * .load("https://www.baidu.com/img/bdlogo.png")
     * .transform(new GlideCircleTransform(context))
     * .into(imageView);
     * Created by Administrator on 2016/12/28.
     *//*
    public static class GlideCircleTransform extends BitmapTransformation {

        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        public Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
*/
}
