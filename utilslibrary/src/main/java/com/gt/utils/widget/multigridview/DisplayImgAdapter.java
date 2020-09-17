package com.gt.utils.widget.multigridview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gt.utils.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;

/**
 * 显示选择的图片Adapter
 * Created by hongsir on 2016/8/4.
 */
public class DisplayImgAdapter extends BaseAdapter {

    private Context ct;
    private List<String> data = new ArrayList<>();
    private int max, numColumns;
    private int addImg, delImg, defImg, cornersRadius;
    private int progress = -1;
    private boolean showEdit = false;
    private int position;
    private OnRemoveListListener onRemoveListListener;

    public static final String qiniu_cache_pic = "https://pic.kuaimashi.com/";
    public static final String endwith_square = "-square";   //方图后缀

    public DisplayImgAdapter(Context ct, int max, int numColumns, int addImg, int delImg, int defImg, int cornersRadius, boolean showEdit) {
        this.ct = ct;
        this.max = max;
        this.numColumns = numColumns;
        this.addImg = addImg;
        this.delImg = delImg;
        this.defImg = defImg;
        this.cornersRadius = cornersRadius;
        this.showEdit = showEdit;
    }

    interface OnRemoveListListener {
        void onRemove(List<String> data, String path, int position);
    }

    public void setOnRemoveListListener(OnRemoveListListener onRemoveListListener) {
        this.onRemoveListListener = onRemoveListListener;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setProgress(int progress, String localPath) {
        this.progress = progress;
        this.position = data.indexOf(localPath);
    }

    public void setEdit(boolean edit) {
        showEdit = edit;
    }

    @Override
    public int getCount() {
        return data.size() > max ? max : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CommonViewHolder viewHolder = CommonViewHolder.getCommonViewHolder(ct, convertView, R.layout.gridview_select_img);
        try {
            if (!TextUtils.isEmpty(data.get(position))) {
                if (showEdit)
                    //如果是编辑模式，就显示删除按钮
                    viewHolder.getImageView(R.id.iv_cancel).setVisibility(View.VISIBLE);
                final ProcessImageView imageView = viewHolder.getView(R.id.iv_img, ProcessImageView.class);
                viewHolder.getImageView(R.id.iv_cancel).setImageResource(delImg);
                RequestBuilder builder = null;
//                if (data.get(position).contains(qiniu_cache_pic)) {
//                    String imgPath = data.get(position) + endwith_square;
//                    builder = Glide.with(ct).load(imgPath);
//                } else
                if (data.get(position).contains("http")) {
//                    String imgPath = data.get(position).replace("_o.", "_m.");
                    String imgPath = data.get(position);
                    builder = Glide.with(ct).load(imgPath);
                } else if (new File(data.get(position)).exists()) {
                    int[] wh = reutrn_bmp_wh(data.get(position));
                    builder = Glide.with(ct).load(data.get(position));
                    if (wh != null && (wh[0] > 1500 || wh[1] > 1500)) {//大图
                        builder.thumbnail(0.2f);
                    }
                    if (this.position == position && progress > -1) {
                        imageView.setShowProgress(true);
                        imageView.setProgress(progress);
                    } else {
                        imageView.setShowProgress(false);
                    }
                } else {
                    imageView.setShowProgress(false);
                    builder = Glide.with(ct).load(addImg);

                    //最后一个是添加按钮，隐藏删除按钮
                    viewHolder.getImageView(R.id.iv_cancel).setVisibility(View.GONE);
                }
                if (builder != null) {
                    builder.diskCacheStrategy(DiskCacheStrategy.ALL);
                    builder.apply(new RequestOptions().placeholder(defImg).error(defImg).centerCrop());
                    builder.transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(cornersRadius)));
                    builder.thumbnail(loadTransform(ct, defImg, cornersRadius));
                    builder.into(imageView);
                }
                viewHolder.getImageView(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onRemoveListListener != null) {//按下删除按钮，触发接口回调
                            onRemoveListListener.onRemove(data, data.get(position), position);
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (position == max) {
            if (viewHolder.convertView != null) viewHolder.convertView.setVisibility(View.GONE);
        } else {
            if (viewHolder.convertView != null) viewHolder.convertView.setVisibility(View.VISIBLE);
        }

        return viewHolder.convertView;
    }

    private static RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId, float radius) {
        return Glide.with(context)
                .load(placeholderId)
                .thumbnail(0.2f)
                .apply(new RequestOptions().centerCrop()
                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) radius))));
    }

    /**
     * 根据本地SD卡图片路径,返回图片尺寸大小
     */
    private int[] reutrn_bmp_wh(String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //不为像素申请内存，只获取图片宽高
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opt);

        //拿到图片宽高
        int wh[] = new int[]{opt.outWidth, opt.outHeight};

        return wh;
    }
}
