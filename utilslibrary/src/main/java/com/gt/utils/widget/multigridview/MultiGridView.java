package com.gt.utils.widget.multigridview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import com.blankj.utilcode.util.ActivityUtils;
import com.gt.photopicker.SelectModel;
import com.gt.photopicker.intent.PhotoPickerIntent;
import com.gt.utils.PermissionUtils;
import com.gt.utils.R;
import com.gt.utils.widget.AntiShake;
import com.gt.utils.widget.ImagePagerActivity;

import java.util.*;

/**
 * 上传多张图片网格控件
 * Created by hongsir on 2016/11/23.
 */
public class MultiGridView extends GridView {

    private Context context;
    private DisplayImgAdapter displayImgAdapter;
    public int maxItems, numColumns;
    public float horizontalSpacing, verticalSpacing, cornersRadius;
    public int addImg, delImg, defImg;
    public int showBn;//ImagePagerActivity.SHOWREMOVEBN显示删除按钮，0-不显示;
    public static float horizontalSpacing_s = -1, verticalSpacing_s = -1, cornersRadius_s = -1;
    public static int maxItems_s = 9, numColumns_s = 3,
            addImg_s = R.drawable.ic_addpic, delImg_s = R.drawable.ic_video_cancel, defImg_s = R.drawable.ic_user_defultimg,
            showBn_s = 0;
    private boolean showEdit;
    public final static String normalPath = "/sdcard/addImage.jpg";//一个添加图片的标识，作为判断，没有实际意义
    public Map<String, String> localPaths = new LinkedHashMap<>();//所有图片本地地址与网络地址对应关系集合
    private OnRemoveListener onRemoveListener;//删除按钮触发接口
    private OnAddListener onAddListener;//添加图片按钮触发接口
    private OnAddedListener onAddedListener;//图片添加后的回调接口

    public static void init(@IntRange(from = 1) int maxItems, @IntRange(from = 1) int numColumns,
                            @DrawableRes int addImg, @DrawableRes int delImg, @DrawableRes int defImg,
                            @IntRange(from = 0) int showBn, @FloatRange(from = 1) float horizontalSpacing,
                            @FloatRange(from = 1) float verticalSpacing, @FloatRange(from = 1) float cornersRadius) {
        maxItems_s = maxItems;
        maxItems_s = numColumns;
        addImg_s = addImg;
        delImg_s = delImg;
        defImg_s = defImg;
        showBn_s = showBn;
        horizontalSpacing_s = horizontalSpacing;
        verticalSpacing_s = verticalSpacing;
        cornersRadius_s = cornersRadius;
    }

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public void setOnAddListenerr(OnAddListener onAddListener) {
        this.onAddListener = onAddListener;
    }

    public void setOnAddedListener(OnAddedListener onAddedListener) {
        this.onAddedListener = onAddedListener;
    }

    public interface OnRemoveListener {
        void onRemove(String path);
    }

    public interface OnAddListener {
        void onAdd();
    }

    public interface OnAddedListener {
        void onAdded();
    }

    public MultiGridView(Context context) {
        this(context, null);
    }

    public MultiGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.MultiGridView, defStyleAttr, 0);

        numColumns = a.getInteger(R.styleable.MultiGridView_android_numColumns, numColumns_s);
        maxItems = a.getInteger(R.styleable.MultiGridView_maxItems, maxItems_s);
        showEdit = a.getBoolean(R.styleable.MultiGridView_showEdit, false);
        if (horizontalSpacing_s == -1) {
            horizontalSpacing_s = 10 * context.getResources().getDisplayMetrics().density;
        }
        horizontalSpacing = a.getDimension(R.styleable.MultiGridView_android_horizontalSpacing, horizontalSpacing_s);
        if (verticalSpacing_s == -1) {
            verticalSpacing_s = 10 * context.getResources().getDisplayMetrics().density;
        }
        verticalSpacing = a.getDimension(R.styleable.MultiGridView_android_verticalSpacing, verticalSpacing_s);
        if (cornersRadius_s == -1) {
            cornersRadius_s = 6 * context.getResources().getDisplayMetrics().density;
        }
        cornersRadius = a.getDimension(R.styleable.MultiGridView_cornersRadius, cornersRadius_s);
        addImg = a.getResourceId(R.styleable.MultiGridView_addImg, addImg_s);
        delImg = a.getResourceId(R.styleable.MultiGridView_delImg, delImg_s);
        defImg = a.getResourceId(R.styleable.MultiGridView_defImg, defImg_s);
        showBn = a.getInteger(R.styleable.MultiGridView_showBn, showBn_s);
        a.recycle();

        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private void init(final Context context) {
        this.context = context;
        this.setHorizontalSpacing((int) horizontalSpacing);
        this.setVerticalSpacing((int) verticalSpacing);
        this.setNumColumns(numColumns);
        this.setSelector(new ColorDrawable(Color.TRANSPARENT));
        displayImgAdapter = new DisplayImgAdapter(this.context, maxItems, numColumns, addImg, delImg, defImg, (int) cornersRadius, showEdit);
        this.setAdapter(displayImgAdapter);
        List<String> images = new ArrayList<>();
        if (showEdit)
            images.add(normalPath);
        this.displayImgAdapter.setData(images);
        this.displayImgAdapter.notifyDataSetChanged();
        this.displayImgAdapter.setOnRemoveListListener(new DisplayImgAdapter.OnRemoveListListener() {
            @Override
            public void onRemove(List<String> data, String path, int position) {
                if (data != null) {
                    data.remove(path);
                    if (showEdit && !displayImgAdapter.getData().contains(normalPath)
                            && displayImgAdapter.getData().size() < maxItems) {
                        displayImgAdapter.getData().add(normalPath);
                    }
                    displayImgAdapter.notifyDataSetChanged();
                    Iterator<String> iterable = localPaths.keySet().iterator();
                    while (iterable.hasNext()) {
                        if (TextUtils.equals(iterable.next(), path)) {
                            iterable.remove();
                        }
                    }
                }
                if (onRemoveListener != null) {
                    onRemoveListener.onRemove(path);
                }
            }
        });

        setOnItemClickListener((parent, view, position, id) -> {
            if (AntiShake.check(view.getId())) {
                return;
            }
            if (itemClick(ActivityUtils.getTopActivity(), position)) {
                if (onAddListener != null)
                    onAddListener.onAdd();
                else
                    new PermissionUtils.Builder(getContext())
                            .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .onGranted(() -> {
                                PhotoPickerIntent intent = new PhotoPickerIntent(getContext());
                                intent.setSelectModel(SelectModel.MULTI);
                                intent.showCarema(true);
                                intent.setMaxTotal(getMaxItems());
                                intent.setSelectedPaths((ArrayList<String>) getLocalPaths());
                                intent.gotoPhotoPickerActivity(getContext(), resultList -> {
                                    setFilenamesData(resultList);
                                    if (onAddedListener != null)
                                        onAddedListener.onAdded();
                                });
                            })
                            .start();
            }
        });
    }

    /**
     * 点击验证查看大图或添加图片
     *
     * @param position 点击位置
     * @return true=添加图片，false=查看大图
     */
    public boolean itemClick(Activity activity, final int position) {
        if (getLocalPaths().size() > position && !TextUtils.isEmpty(getLocalPaths().get(position))) {
            //点击的是已选择的图片，查看大图，删除图片
            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(this.getMeasuredWidth(), this.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(activity, getLocalPaths(), position,
                    imageSize, showBn);
            ((Activity) context).overridePendingTransition(R.anim.large_in, 0);
            return false;
        }
        return true;
    }

    public void updateProgress(final String localPath, final int progress) {
        displayImgAdapter.setProgress(progress, localPath);
        displayImgAdapter.notifyDataSetChanged();
    }

    public void updateSuccess(String localPath, String url) {
        updateProgress(localPath, 100);
        if (localPaths.containsKey(localPath)) {
            localPaths.put(localPath, url);
        }
    }

    //有草稿时，初始化
    public void setFilenamesData(List<String> initData) {
        if (initData != null) {
            displayImgAdapter.getData().remove(normalPath);
            for (String str : initData) {
                if (!TextUtils.isEmpty(str) && !localPaths.containsKey(str)) {
                    localPaths.put(str, str);
                    displayImgAdapter.getData().add(str);
                }
            }
            if (showEdit && displayImgAdapter.getData().size() < maxItems) {
                displayImgAdapter.getData().add(normalPath);
            }
        }
        displayImgAdapter.notifyDataSetChanged();
    }

    public List<String> getPaths() {
        List<String> list = new ArrayList<>();
        for (String key : localPaths.keySet()) {
            list.add(localPaths.get(key));
        }
        list.remove(normalPath);
        return list;
    }

    public List<String> getLocalPaths() {
        return new ArrayList<>(localPaths.keySet());
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setEdit(boolean edit) {
        showEdit = edit;
        if (showEdit) {
            if (displayImgAdapter.getData().size() < maxItems
                    && !displayImgAdapter.getData().contains(normalPath)) {
                displayImgAdapter.getData().add(normalPath);
            }
        } else {
            displayImgAdapter.getData().remove(normalPath);
        }
        displayImgAdapter.setEdit(showEdit);
        displayImgAdapter.notifyDataSetChanged();
    }
}
