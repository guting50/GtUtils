package com.gt.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gt.utils.R;
import com.gt.utils.widget.multigridview.MultiGridView;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 查看大图
 *
 * @author hongsir
 * Created by yiw on 2016/1/6.
 */
public class ImagePagerActivity extends AppCompatActivity {

    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";
    public static final String INTENT_SHOWBN = "showbn";
    public static final int BACK_RESULT = 999;
    public static final int SHOWREMOVEBN = 1;
    public static final int SHOWSAVEBN = 2;

    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;
    public ImageSize imageSize;
    private TextView tvCountShow;
    private TextView mBn;
    private TextView mHint;
    private int startPos;
    private ArrayList<String> imgUrls;
    private int currentPosition;
    private int showBn; // 1显示删除，2显示保存
    private ViewPager mViewPager;
    private ImageAdapter mAdapter;

    public static void startImagePagerActivity(Activity context, List<String> imgUrls, int position) {
        startImagePagerActivity(context, imgUrls, position, null, 0);
    }

    public static void startImagePagerActivity(Activity context, List<String> imgUrls, int position, ImageSize imageSize) {
        startImagePagerActivity(context, imgUrls, position, imageSize, 0);
    }

    public static void startImagePagerActivity(Activity context, List<String> imgUrls, int position, ImageSize imageSize, int showBn) {
        startImagePagerActivity(context, imgUrls, position, imageSize, showBn, 0);
    }

    public static void startImagePagerActivity(Activity context, List<String> imgUrls,
                                               int position, ImageSize imageSize, int showBn, int requestCode) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        intent.putExtra(INTENT_SHOWBN, showBn);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imagepager);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        guideGroup = (LinearLayout) findViewById(R.id.guideGroup);
        mBn = (TextView) findViewById(R.id.bn);
        mHint = (TextView) findViewById(R.id.hint);
        tvCountShow = (TextView) findViewById(R.id.tv_count_show);

        getIntentData();

        switch (showBn) {
            case SHOWREMOVEBN:
                mBn.setVisibility(View.VISIBLE);
                mBn.setText("删除");
                break;
            case SHOWSAVEBN:
                mBn.setVisibility(View.VISIBLE);
                mBn.setText("保存");
                break;
            default:
                mBn.setVisibility(View.GONE);
                break;
        }

        mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //滑动后初始化 "前一张" 图片
                ImageAdapter adapter = (ImageAdapter) mViewPager.getAdapter();
                String imgurl = adapter.getDatas().get(currentPosition);
                ImageView imageView = adapter.getImageViews().get(currentPosition);
                if (imageView != null)
                    adapter.loadImg(imgurl, imageView, null);

                currentPosition = position;
                tvCountShow.setText((position + 1) + "/" + imgUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(startPos);
        tvCountShow.setText((startPos + 1) + "/" + imgUrls.size());

//        addGuideView(guideGroup, startPos, imgUrls);

        mBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (showBn) {
                    case SHOWREMOVEBN:
                        if ("back".equals(view.getTag())) {
                            onBackPressed();
                            return;
                        }
//                        guideGroup.removeAllViews();
                        if (mAdapter.getCount() > 0) {
                            imgUrls.remove(currentPosition);
                            if (currentPosition > 0)
                                currentPosition--;
                            if (mAdapter.getCount() == 1) { // 防止只有一张图片的时候还能滑动
                                mViewPager.setAdapter(mAdapter);
                                tvCountShow.setText("1/" + imgUrls.size());
                            } else if (mAdapter.getCount() == 0) {
                                mViewPager.setAdapter(null);
                                guideGroup.removeAllViews();
                                mHint.setVisibility(View.VISIBLE);
                                mBn.setText("返回");
                                mBn.setTag("back");
                            } else {
                                mAdapter.setDatas(imgUrls).notifyDataSetChanged();
                                mViewPager.setCurrentItem(currentPosition);
                                tvCountShow.setText((currentPosition + 1) + "/" + imgUrls.size());
//                                addGuideView(guideGroup, currentPosition, imgUrls);
                            }
                        }
                        break;
                    case SHOWSAVEBN:
                        String KMS_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "kms";
                        String PHOTO_DIR = KMS_DIR + File.separator + "images";
                        String name = PHOTO_DIR + File.separator + "exhibition_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".jpg";
                        ImageUtils.save(ImageUtils.getBitmap(imgUrls.get(currentPosition)), name, Bitmap.CompressFormat.JPEG);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取参数
     */
    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);
        showBn = getIntent().getIntExtra(INTENT_SHOWBN, -1);
    }

    /**
     * 底部指示位置圆点
     *
     * @param guideGroup 父容器
     * @param startPos   开始位置
     * @param imgUrls    url集合
     */
    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if (imgUrls != null && mAdapter.getCount() > 1) {// 只有一张图片的时候不显示圆点
            guideViewList.clear();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i == startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(6 * (int) getResources().getDisplayMetrics().density,
                        6 * (int) getResources().getDisplayMetrics().density);
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        setResult(BACK_RESULT, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        //离开动画
        overridePendingTransition(0, R.anim.small_out);
    }

    private class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private Context context;
        private ImageSize imageSize;
        private ImageView smallImageView = null;
        private Map<Integer, ImageView> imageViews = new HashMap<>();

        public ImageAdapter setDatas(List<String> datas) {
            if (datas != null)
                this.datas = datas;
            return this;
        }

        public List<String> getDatas() {
            return datas;
        }

        public Map<Integer, ImageView> getImageViews() {
            return imageViews;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            if (datas == null) return 0;
            if (datas.contains(MultiGridView.normalPath))
                return datas.size() - 1;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if (view != null) {
                final ImageView imageView = (ImageView) view.findViewById(R.id.ijk_background);
                final ImageView iv_back = (ImageView) view.findViewById(R.id.iv_back);
                imageViews.put(position, imageView);

                if (imageSize != null) {
                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout) view).addView(smallImageView);
                }

                //loading
                ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

                String imgurl = datas.get(position);
                loadImg(imgurl, imageView, loading);
                // 单击退出图片浏览
               /* ((PhotoView) imageView).setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    public void onPhotoTap(View view, float x, float y) {
                        ((ImagePagerActivity) context).onBackPressed();
                    }

                    public void onOutsidePhotoTap() {
                        ((ImagePagerActivity) context).onBackPressed();
                    }
                });*/
                container.addView(view, 0);
                iv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
            return view;
        }

        public void loadImg(String imgurl, final ImageView imageView, final ProgressBar loading) {
            //ImagesUtil.displayImage(imgurl, imageView, R.drawable.ic_img_waiting, R.drawable.ic_user_defultimg);
            Glide.with(context)
                    .load(imgurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存多个尺寸
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    //.placeholder(R.drawable.ic_img_waiting)//部分机型第一次显示图片变形的问题*/
                    .error(R.drawable.ic_user_defultimg)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                            if (loading != null)
                                loading.setVisibility(View.GONE);
                        }
                    });
//                    .into(new DrawableImageViewTarget(imageView) {
//                        @Override
//                        public void onLoadStarted(Drawable placeholder) {
//                            super.onLoadStarted(placeholder);
//                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                            if (loading != null)
//                                loading.setVisibility(View.GONE);
//                        }
//                    });
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }

    @Override
    protected void onDestroy() {
        guideViewList.clear();
        super.onDestroy();
    }

    public static class ImageSize implements Serializable {
        /**
         * 图片宽度
         */
        private int width;
        /**
         * 图片高度
         */
        private int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        Intent mediaScanIntent = new Intent(
                                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri contentUri = Uri.fromFile(new File(name)); //out is your output file
                                        mediaScanIntent.setData(contentUri);
                                        sendBroadcast(mediaScanIntent);
                                    } else {
                                        sendBroadcast(new Intent(
                                                Intent.ACTION_MEDIA_MOUNTED,
                                                Uri.parse("file://"
                                                        + Environment.getExternalStorageDirectory())));
                                    }*/
}
